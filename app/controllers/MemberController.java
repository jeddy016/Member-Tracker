package controllers;

import models.*;
import play.data.DynamicForm;
import play.data.FormFactory;
import play.db.jpa.JPAApi;
import play.db.jpa.Transactional;
import play.mvc.Controller;
import play.mvc.Result;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

public class MemberController extends Controller
{
    private final JPAApi jpaApi;
    private final FormFactory formFactory;

    @Inject
    public MemberController(JPAApi jpaApi, FormFactory formFactory)
    {
        this.jpaApi = jpaApi;
        this.formFactory = formFactory;
    }

    @Transactional
    public Result renderAddMember()
    {
        List<Chapter> chapters = jpaApi.em().createQuery("SELECT c FROM Chapter c", Chapter.class).getResultList();
        List<String> errors = new ArrayList<>();
        return ok(views.html.addMember.render(chapters, errors));
    }

    @Transactional
    public Result addMember()
    {
        DynamicForm form = formFactory.form().bindFromRequest();

        MemberForm memberForm = new MemberForm();

        memberForm.setFirstName(form.get("firstName"));
        memberForm.setLastName(form.get("lastName"));
        memberForm.setEmail(form.get("email"));
        memberForm.setPhone(form.get("phone"));
        memberForm.setCompany(form.get("company"));
        memberForm.setJobTitle(form.get("job"));
        memberForm.setDateJoined(form.get("joinDate"));

        if(memberForm.isValid())
        {
            int chapter = Integer.parseInt(form.get("chapter"));
            int volunteer = Integer.parseInt(form.get("volunteer"));
            int companyID = getCompanyID(memberForm.getCompany());
            int jobTitleID = getJobTitleID(memberForm.getJobTitle());

            Member member = new Member();

            member.setFirstName(memberForm.getFirstName());
            member.setLastName(memberForm.getLastName());
            member.setEmail(memberForm.getEmail());
            member.setPhone(memberForm.getPhone());
            member.setJobTitleID(jobTitleID);
            member.setCompanyID(companyID);
            member.setDateJoined(memberForm.getDateJoined());
            member.setChapterID(chapter);
            member.setVolunteer(volunteer);
            member.setActive(1);

            jpaApi.em().persist(member);
        }
        else
        {
            List<Chapter> chapters = jpaApi.em().createQuery("SELECT c FROM Chapter c", Chapter.class).getResultList();
            List<String> errors = memberForm.showErrors();

            return ok(views.html.addMember.render(chapters, errors));
        }
        return redirect(routes.BaseController.index());
    }

    @Transactional
    public Result renderEditMember(Integer id)
    {
        String query = "SELECT m.member_id as id, m.first_name as firstName, m.last_name as lastName, m.email, m.phone, ch.name as chapter, m.date_joined as dateJoined, m.volunteer, m.active, co.name as company, jt.name as jobTitle FROM member m  JOIN company co ON m.company_id = co.company_id  JOIN chapter ch ON m.chapter_id = ch.chapter_id JOIN job_title jt ON m.job_title_id = jt.job_title_id WHERE m.member_id = :id";

        MemberDetail member = (MemberDetail) jpaApi.em()
                .createNativeQuery(query, MemberDetail.class)
                .setParameter("id", id)
                .getSingleResult();

        List<Chapter> chapters = jpaApi.em().createQuery("SELECT c FROM Chapter c", Chapter.class).getResultList();
        List<String> errors = new ArrayList<>();

        return ok(views.html.editMember.render(member, chapters, errors));
    }

    @Transactional
    public Result editMember(Integer id)
    {
        DynamicForm form = formFactory.form().bindFromRequest();

        MemberForm memberForm = new MemberForm();

        memberForm.setFirstName(form.get("firstName"));
        memberForm.setLastName(form.get("lastName"));
        memberForm.setEmail(form.get("email"));
        memberForm.setPhone(form.get("phone"));
        memberForm.setCompany(form.get("company"));
        memberForm.setJobTitle(form.get("job"));
        memberForm.setDateJoined(form.get("joinDate"));

        if(memberForm.isValid())
        {
            int chapter = Integer.parseInt(form.get("chapter"));
            int volunteer = Integer.parseInt(form.get("volunteer"));
            int companyID = getCompanyID(memberForm.getCompany());
            int jobTitleID = getJobTitleID(memberForm.getJobTitle());

            jpaApi.em().createQuery("UPDATE Member SET firstName = :first, lastName = :last, email = :email, phone = :phone, companyID = :companyID, jobTitleID = :jobTitleID, volunteer = :volunteer, chapterID = :chapter, dateJoined = :dateJoined WHERE id = :id")
                    .setParameter("first", memberForm.getFirstName())
                    .setParameter("last", memberForm.getLastName())
                    .setParameter("email", memberForm.getEmail())
                    .setParameter("phone", memberForm.getPhone())
                    .setParameter("companyID", companyID)
                    .setParameter("jobTitleID",jobTitleID)
                    .setParameter("volunteer", volunteer)
                    .setParameter("chapter", chapter)
                    .setParameter("dateJoined", memberForm.getDateJoined())
                    .setParameter("id", id)
                    .executeUpdate();
        }
        else
        {
            String query = "SELECT m.member_id as id, m.first_name as firstName, m.last_name as lastName, m.email, m.phone, ch.name as chapter, m.date_joined as dateJoined, m.volunteer, co.name as company, jt.name as jobTitle FROM member m  JOIN company co ON m.company_id = co.company_id  JOIN chapter ch ON m.chapter_id = ch.chapter_id JOIN job_title jt ON m.job_title_id = jt.job_title_id WHERE m.member_id = :id";

            MemberDetail member = (MemberDetail) jpaApi.em()
                    .createNativeQuery(query, MemberDetail.class)
                    .setParameter("id", id)
                    .getSingleResult();

            List<Chapter> chapters = jpaApi.em().createQuery("SELECT c FROM Chapter c", Chapter.class).getResultList();
            List<String> errors = memberForm.showErrors();

            return ok(views.html.editMember.render(member, chapters, errors));
        }

        return redirect(routes.BaseController.index());
    }

    @Transactional
    public Result deleteMember(Integer id)
    {
        String query = "UPDATE member SET active = 0 WHERE member_id = :id";

        jpaApi.em().createNativeQuery(query).setParameter("id", id).executeUpdate();

        return redirect(routes.BaseController.index());
    }

    @Transactional
    private int getCompanyID(String name)
    {
        Integer id = -1;

        if(name.length() > 0)
        {
            try
            {
                Company company = (Company) jpaApi.em().createNativeQuery("SELECT * FROM company WHERE name LIKE :name", Company.class).setParameter("name", name).setMaxResults(1).getSingleResult();

                id = company.getId();

            } catch (Exception e)
            {
                jpaApi.em().createNativeQuery("INSERT INTO company(name) VALUES(:name)").setParameter("name", name).executeUpdate();

                id = (Integer) jpaApi.em().createNativeQuery("SELECT company_id FROM company WHERE name LIKE :name").setParameter("name", name).getSingleResult();
            }
        }

        return id;
    }

    @Transactional
    private int getJobTitleID(String name)
    {
        Integer id = -1;

        if(name.length() > 0)
        {
            try
            {
                JobTitle title = (JobTitle) jpaApi.em().createNativeQuery("SELECT * FROM job_title WHERE name LIKE :name", JobTitle.class).setParameter("name", name).setMaxResults(1).getSingleResult();

                id = title.getId();

            } catch (Exception e)
            {
                jpaApi.em().createNativeQuery("INSERT INTO job_title(name) VALUES(:name)").setParameter("name", name).executeUpdate();

                id = (Integer) jpaApi.em().createNativeQuery("SELECT job_title_id FROM job_title WHERE name LIKE :name").setParameter("name", name).getSingleResult();
            }
        }

        return id;
    }
}
