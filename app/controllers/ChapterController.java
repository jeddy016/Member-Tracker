package controllers;

import models.Chapter;
import models.ChapterDetail;
import models.ChapterFormHelper;
import models.Member;
import play.data.DynamicForm;
import play.data.FormFactory;
import play.db.jpa.JPAApi;
import play.db.jpa.Transactional;
import play.mvc.Controller;
import play.mvc.Result;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

public class ChapterController extends Controller
{
    private final JPAApi jpaApi;
    private final FormFactory formFactory;

    @Inject
    public ChapterController(JPAApi jpaApi, FormFactory formFactory)
    {
        this.jpaApi = jpaApi;
        this.formFactory = formFactory;
    }

    @Transactional
    public Result addChapter()
    {
        DynamicForm form = formFactory.form().bindFromRequest();
        ChapterFormHelper formHelper = new ChapterFormHelper();
        formHelper.fillForm(form);

        if(!formHelper.isValid())
        {
            List<String> errors = formHelper.showErrors();
            return ok(views.html.addChapter.render(errors));
        }

        Chapter chapter = new Chapter(form);
        jpaApi.em().persist(chapter);

        return ok(views.html.chapters.render(getChapters()));
    }

    @Transactional
    public Result editChapter(int id)
    {
        DynamicForm form = formFactory.form().bindFromRequest();
        ChapterFormHelper formHelper = new ChapterFormHelper();
        formHelper.fillForm(form);

        if(!formHelper.isValid())
        {
            List<String> errors = formHelper.showErrors();
            List<Member> members = jpaApi.em().createQuery("SELECT m FROM Member m WHERE chapterID = :id", Member.class).setParameter("id", id).getResultList();
            ChapterDetail chapter = (ChapterDetail) jpaApi.em().createNativeQuery("SELECT c.chapter_id AS id, c.name AS name, c.meeting_place AS meetingPlace, c.street_address AS streetAddress, c.city AS city, c.leader_id AS leaderID, (SELECT CONCAT(first_name, ' ',last_name) FROM member WHERE member_id = c.leader_id) AS leader, (SELECT email FROM member WHERE member_id = c.leader_id) AS leaderEmail FROM chapter c WHERE c.chapter_id = :id ORDER BY c.name;", ChapterDetail.class).setParameter("id", id).getSingleResult();
            return ok(views.html.editChapter.render(chapter, members, errors));
        }

        Chapter chapter = new Chapter(form);
        chapter.setID(id);
        jpaApi.em().merge(chapter);

        return ok(views.html.chapters.render(getChapters()));
    }

    @Transactional
    public Result deleteChapter(int id)
    {
        jpaApi.em().createNativeQuery("DELETE FROM chapter WHERE chapter_id = :id").setParameter("id", id).executeUpdate();
        return ok(views.html.chapters.render(getChapters()));
    }

    @Transactional
    public List<ChapterDetail> getChapters()
    {
        @SuppressWarnings("unchecked")
        List<ChapterDetail> chapters = jpaApi.em().createNativeQuery("SELECT c.chapter_id AS id, c.name AS name, c.meeting_place AS meetingPlace, c.street_address AS streetAddress, c.city AS city, c.leader_id AS leaderID, (SELECT CONCAT(first_name, ' ',last_name) FROM member WHERE member_id = c.leader_id) AS leader, (SELECT email FROM member WHERE member_id = c.leader_id) AS leaderEmail FROM chapter c  ORDER BY c.name;", ChapterDetail.class).getResultList();
        return chapters;
    }
}
