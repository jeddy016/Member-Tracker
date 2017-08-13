package controllers;

import models.Chapter;
import models.Member;
import models.MemberDetail;
import models.MemberForm;
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
        List<Chapter> chapters = jpaApi.em().createQuery("SELECT c FROM Chapter c", Chapter.class).getResultList();

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

            Member member = new Member();

            member.setFirstName(memberForm.getFirstName());
            member.setLastName(memberForm.getLastName());
            member.setEmail(memberForm.getEmail());
            member.setPhone(memberForm.getPhone());
            member.setJobTitleID(1);
            member.setCompanyID(1);
            member.setDateJoined(memberForm.getDateJoined());
            member.setChapterID(chapter);
            member.setVolunteer(volunteer);

            jpaApi.em().persist(member);
        }
        else
        {
            return ok(views.html.addMember.render(chapters, memberForm.showErrors()));
        }

        return redirect(routes.BaseController.index());
    }

    @Transactional
    public Result renderEditMember(Integer id)
    {
        String query = "SELECT m.member_id as id, m.first_name as firstName, m.last_name as lastName, m.email, m.phone, ch.name as chapter, m.date_joined as dateJoined, m.volunteer, co.name as company, jt.name as jobTitle FROM member m  JOIN company co ON m.company_id = co.company_id  JOIN chapter ch ON m.chapter_id = ch.chapter_id JOIN job_title jt ON m.job_title_id = jt.job_title_id WHERE m.member_id = :id";

        MemberDetail member = (MemberDetail) jpaApi.em()
                .createNativeQuery(query, MemberDetail.class)
                .setParameter("id", id)
                .getSingleResult();

        List<Chapter> chapters = jpaApi.em().createQuery("SELECT c FROM Chapter c", Chapter.class).getResultList();

        return ok(views.html.editMember.render(member, chapters));
    }

    @Transactional
    public Result editMember(Integer id)
    {

        return ok();
    }
}
