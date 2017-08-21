package controllers;

import models.Chapter;
import models.ChapterDetail;
import models.Member;
import models.MemberDetail;
import play.data.FormFactory;
import play.db.jpa.JPAApi;
import play.db.jpa.Transactional;
import play.mvc.Controller;
import play.mvc.Result;

import javax.inject.Inject;
import java.util.ArrayList;
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

    @Transactional
    public Result renderAddMember()
    {
        List<Chapter> chapters = jpaApi.em().createQuery("SELECT c FROM Chapter c", Chapter.class).getResultList();
        List<String> errors = new ArrayList<>();
        return ok(views.html.addMember.render(chapters, errors));
    }

    @Transactional
    public Result renderEditMember(Integer id)
    {
        MemberDetail member = getSingleMember(id);
        List<Chapter> chapters = jpaApi.em().createQuery("SELECT c FROM Chapter c", Chapter.class).getResultList();
        List<String> errors = new ArrayList<>();
        return ok(views.html.editMember.render(member, chapters, errors));
    }

    @Transactional
    public Result renderChapters()
    {
        List<ChapterDetail> chapters = getChapters();
        return ok(views.html.chapters.render(chapters));
    }

    public Result renderAddChapter()
    {
        return ok("add chapter");
    }

    @Transactional
    public List<ChapterDetail> getChapters()
    {
        String query = "SELECT c.chapter_id AS id, c.name AS name, c.meeting_place AS meetingPlace, c.street_address AS streetAddress, c.city AS city, (SELECT CONCAT(first_name, ' ',last_name) FROM member WHERE member_id = c.leader_id) AS leader, (SELECT email FROM member WHERE member_id = c.leader_id) AS leaderEmail FROM chapter c ORDER BY c.name;";

        return jpaApi.em().createNativeQuery(query, ChapterDetail.class).getResultList();
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
