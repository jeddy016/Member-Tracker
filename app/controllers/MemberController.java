package controllers;

import models.*;
import play.data.DynamicForm;
import play.data.FormFactory;
import play.db.jpa.JPAApi;
import play.db.jpa.Transactional;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.editMember;

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
    public Result addMember()
    {
        DynamicForm form = formFactory.form().bindFromRequest();
        MemberFormHelper formHelper = new MemberFormHelper();
        formHelper.fillForm(form);

        if(!formHelper.isValid())
        {
            List<Chapter> chapters = getChapters();
            List<String> errors = formHelper.showErrors();
            return ok(views.html.addMember.render(chapters, errors));
        }

        Member member = new Member(formHelper);
        member.setCompanyID(getCompanyID(formHelper.getCompany()));
        member.setJobTitleID(getJobTitleID(formHelper.getJobTitle()));
        jpaApi.em().persist(member);

        return redirect(routes.BaseController.index());
    }

    @Transactional
    public Result editMember(Integer id)
    {
        DynamicForm form = formFactory.form().bindFromRequest();
        MemberFormHelper formHelper = new MemberFormHelper();
        formHelper.fillForm(form);

        if(!formHelper.isValid())
        {
            MemberDetail member = getSingleMember(id);
            List<Chapter> chapters = getChapters();
            List<String> errors = formHelper.showErrors();
            return ok(editMember.render(member, chapters, errors));
        }

        Member member = new Member(formHelper);
        member.setCompanyID(getCompanyID(formHelper.getCompany()));
        member.setJobTitleID(getJobTitleID(formHelper.getJobTitle()));
        member.setId(id);
        jpaApi.em().merge(member);

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

    @Transactional
    private List<Chapter> getChapters()
    {
        return jpaApi.em().createQuery("SELECT c FROM Chapter c", Chapter.class).getResultList();
    }

    @Transactional
    private MemberDetail getSingleMember(int id)
    {
        String query = "SELECT m.member_id as id, m.first_name as firstName, m.last_name as lastName, m.email, m.phone, m.active, ch.name as chapter, m.date_joined as dateJoined, m.volunteer, co.name as company, jt.name as jobTitle FROM member m  JOIN company co ON m.company_id = co.company_id  JOIN chapter ch ON m.chapter_id = ch.chapter_id JOIN job_title jt ON m.job_title_id = jt.job_title_id WHERE m.member_id = :id";

        return (MemberDetail) jpaApi.em()
                .createNativeQuery(query, MemberDetail.class)
                .setParameter("id", id)
                .getSingleResult();
    }
}
