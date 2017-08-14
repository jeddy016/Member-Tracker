package controllers;

import models.Chapter;
import models.Member;
import models.MemberDetail;
import play.data.FormFactory;
import play.db.jpa.JPAApi;
import play.db.jpa.Transactional;
import play.mvc.Controller;
import play.mvc.Result;

import javax.inject.Inject;
import java.util.List;

import static play.mvc.Results.ok;

public class BaseController extends Controller
{
    private final JPAApi jpaApi;

    @Inject
    public BaseController(JPAApi jpaApi, FormFactory formFactory)
    {
        this.jpaApi = jpaApi;
    }

    @Transactional
    public Result index()
    {
        @SuppressWarnings("unchecked")
        List<MemberDetail> members = jpaApi.em().createNativeQuery("SELECT m.member_id as id, m.first_name as firstName, m.last_name as lastName, m.email, m.phone, ch.name as chapter, m.date_joined as dateJoined, m.volunteer, co.name as company, jt.name as jobTitle, m.active as active FROM member m JOIN company co ON m.company_id = co.company_id  JOIN chapter ch ON m.chapter_id = ch.chapter_id JOIN job_title jt ON m.job_title_id = jt.job_title_id WHERE m.active = 1 ORDER BY lastName", MemberDetail.class).getResultList();

        List<Chapter> chapters = jpaApi.em().createQuery("SELECT c FROM Chapter c", Chapter.class).getResultList();

        return ok(views.html.index.render(members, chapters));
    }

}
