package irepdata.controller;

import irepdata.dto.IdeaDummy;
import irepdata.model.Comment;
import irepdata.model.Idea;
import irepdata.model.Tag;
import irepdata.model.User;
import irepdata.service.*;
import irepdata.support.CookiesHandler;
import irepdata.support.IdeaDummyConversionTool;
import irepdata.support.TagSupport;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by Gvozd on 04.12.2016.
 */

@Controller
public class MainController {
    public static final String URLCLASSPREFIX = "/ideas/";
    final Log logger = LogFactory.getLog(MainController.class);

    @Autowired
    private IdeaService ideaService;
    @Autowired
    private UserService userService;
    @Autowired
    private TagService tagService;
    @Autowired
    private CommentService commentService;
    @Autowired
    private ContentService contentService;
    @Autowired
    private TagSupport tagSupport;

    //LOGOUT
    @RequestMapping(URLCLASSPREFIX + "logout")
    public String mainToIndexLogout() {
        return "redirect:/logout";
    }

    //LIST
    @RequestMapping(URLCLASSPREFIX + "list")
    public String listOfIdeas(Map<String, Object> map, HttpServletRequest request) {
        map.put("ideaList", ideaService.getSortedIdeaListWithoutDisabled(true, "posted"));
        request.getSession().removeAttribute("IMAGE_OFFSET");
        return "idealistpage";
    }

    //CABINET
    @RequestMapping(URLCLASSPREFIX + "cabinet")
    public String cabinet(Map<String, Object> map, HttpServletRequest request) {
        Long myId = (Long) request.getSession().getAttribute("USER_ID");
        map.put("ideaList", ideaService.getSortedIdeaListForUser(myId, true, "posted"));
        return "cabinetpage";
    }

    //SELECT IDEA AND VIEW IT DATA
    @RequestMapping(value = URLCLASSPREFIX + "/showidea/{ideaId}")
    public String selectIdea(@PathVariable("ideaId") Long ideaId, Map<String, Object> map, HttpServletRequest request, HttpServletResponse response) {
        Idea idea = ideaService.getIdeaWithAllDataById(ideaId);
        Long userId = (Long) request.getSession().getAttribute("USER_ID");
        if (isIdeaLiked(ideaId, userId, request)) request.setAttribute("notshowlikes", true);
        if (!isWatched(idea, userId, request)) ideaService.watch(ideaId);
        logger.info(idea.getName() + " in maincontroller - loaded!");
        request.setAttribute("RURI", "/ideas/list");
        request.getSession().setAttribute("TGTIDEA",idea.getId());
        request.getSession().setAttribute("RETURNTO", "/ideas/showidea/"+ideaId);
        map.put("searchable", idea);
        map.put("comment", new Comment());
        return "showidea";
    }

    //SELECT OWN IDEA AND VIEW IT DATA
    @RequestMapping(value = URLCLASSPREFIX + "/showmyidea/{ideaId}")
    public String selectMyIdea(@PathVariable("ideaId") Long ideaId, Map<String, Object> map, HttpServletRequest request, HttpServletResponse response) {
        Idea idea = ideaService.getIdeaWithAllDataById(ideaId);
        logger.info(idea.getName() + " in maincontroller - loaded!");
        request.setAttribute("RURI", "/ideas/cabinet");
        request.getSession().setAttribute("TGTIDEA",idea.getId());
        request.getSession().setAttribute("RETURNTO", "/ideas/showmyidea/"+ideaId);
        map.put("searchable", idea);
        map.put("comment", new Comment());
        return "showmyidea";
    }

    //CREATE IDEA
    @RequestMapping(URLCLASSPREFIX + "create")
    public String createIdea(@ModelAttribute("ideaData") IdeaDummy ideaDummy, BindingResult result) {
        IdeaDummy ideaData = new IdeaDummy();
        return "createidea";
    }

    //CREATE IDEA HANDLER
    @RequestMapping(value = URLCLASSPREFIX + "сreateideahandler", method = RequestMethod.POST)
    public String addingIdea(@ModelAttribute("ideaData") IdeaDummy ideaDummy,
                              BindingResult result, HttpServletRequest request) {
        Long authorId = (Long) request.getSession().getAttribute("USER_ID");
        User author = userService.getUserById(authorId);

        Set<Tag> tagSet = tagSupport.parseTagsFromStringToSet(ideaDummy.getTags());

        Idea idea = new Idea();
        idea.setTags(tagSet);
        idea.setAuthor(author);
        idea.setViewedCount(0L);
        idea.setPosted(new Timestamp(System.currentTimeMillis()));
        idea.setViewed(new Timestamp(System.currentTimeMillis()));
        idea.setLiked(0);
        idea.setDisliked(0);

        IdeaDummyConversionTool.fillIdeaFromDummy(ideaDummy, idea);

        ideaService.createIdea(idea);
        return "redirect:/ideas/cabinet";
    }

    //EDIT OWN IDEA
    @RequestMapping(value = URLCLASSPREFIX + "/editmyidea/{ideaId}")
    public String editMyIdea(@PathVariable("ideaId") Long ideaId, Model model,
                             HttpServletRequest request, HttpServletResponse response) {
        Idea idea = ideaService.getIdeaWithAllDataById(ideaId);

        Long userId = (Long) request.getSession().getAttribute("USER_ID");
        Boolean isAdmin = (Boolean) request.getSession().getAttribute("IS_ADMIN");
        if (!userId.equals(idea.getAuthor().getId()) && (!isAdmin)){
            try {
                response.sendRedirect("/ideas/list");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        logger.info(idea.getName() + " in maincontroller - loaded!");

        Map<String, String> isEnabled = new LinkedHashMap<String, String>();
        IdeaDummy idummy = new IdeaDummy();
        IdeaDummyConversionTool.fillDummyFromIdea(idea, idummy);
        isEnabled.put("true", "True");
        isEnabled.put("false", "False");
        model.addAttribute("ideaAttrib", idummy);
        model.addAttribute("enablind", isEnabled);
        request.getSession().setAttribute("TARGETIDEA", ideaId);
        return "editmyideapage";
    }

    //EDIT OWN IDEA HANDLER
    @RequestMapping(value = URLCLASSPREFIX + "editideahandler", method = RequestMethod.POST)
    public String editMyIdeaHandler(@ModelAttribute("ideaAttrib") IdeaDummy ideaDummy, BindingResult result,
                                    HttpServletRequest request) {
        System.out.println(ideaDummy.toString());
        Long targetIdeaId = (Long) request.getSession().getAttribute("TARGETIDEA");
        Long targetContentId = ideaService.getIdeaById(targetIdeaId).getContentId();
        ideaService.updateIdea(targetIdeaId, ideaDummy.getName(),ideaDummy.getDescription(),ideaDummy.getImage(),ideaDummy.getTags(),ideaDummy.isEnabled(),targetContentId,ideaDummy.getContent());
        request.getSession().removeAttribute("TARGETIDEA");
        return "redirect:/ideas/cabinet";
    }

    //MESSAGE HANDLER
    @RequestMapping(value = URLCLASSPREFIX + "messagehandler", method = RequestMethod.POST)
    public void commentHandler(@ModelAttribute("message") Comment comment, BindingResult result,
                                    HttpServletRequest request, HttpServletResponse response) {
        if (null==request.getSession().getAttribute("USER_ID")){
            try {
                response.sendRedirect("/index");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        System.out.println(comment.toString());
        String redirect = (String) request.getSession().getAttribute("RETURNTO");
        Long userId = (Long) request.getSession().getAttribute("USER_ID");
        Long ideaId = (Long) request.getSession().getAttribute("TGTIDEA");
        User user = userService.getUserById(userId);
        Idea idea = ideaService.getIdeaById(ideaId);

        comment.setIdea(idea);
        comment.setAuthor(user);
        comment.setEnabled(true);
        comment.setPosted(new Timestamp(System.currentTimeMillis()));
        commentService.createComment(comment);

        request.getSession().removeAttribute("RETURNTO");
        request.getSession().removeAttribute("TGTIDEA");
        try {
            response.sendRedirect(redirect);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //LIKE IDEA
    @RequestMapping(value = URLCLASSPREFIX + "/likeidea/{ideaId}&like={doLike}")
    public String likeIdea(@PathVariable("ideaId") Long ideaId, @PathVariable("doLike") Boolean doLike,HttpServletRequest request, HttpServletResponse response) throws IOException {
        Boolean redirectExceeption = false;
        String redirect = (String) request.getSession().getAttribute("RETURNTO");
        Long myId = (Long) request.getSession().getAttribute("USER_ID");
        if (isIdeaLiked(ideaId, myId, request)) return "redirect:/index";

        if ((null==myId) || (null==doLike)){
            System.out.println("MYID=NULL OR DOLIKE=NULL");
            return "redirect:/index";
        } else if (myId.equals(ideaService.getIdeaById(ideaId).getAuthor().getId())){
            System.out.println("MY ID IS AUTHOR ID");
            return "redirect:/ideas/showidea/"+ideaId;
        }

        if (doLike) {
            System.out.println("liked it");
            ideaService.like(ideaId);
        } else {
            System.out.println("disliked it");
            ideaService.dislike(ideaId);
        }

        likeIdea(ideaId, myId, response);
        return "redirect:/ideas/showidea/"+ideaId;
    }

    //SUPPORT METHOD FOR LIKE SYSTEM
    private void likeIdea(Long ideaId, Long userId, HttpServletResponse response){
        CookiesHandler.setCookie(userId.toString()+"donotlike"+ideaId, "true", 60*60*7, response);
    }

    //SUPPORT METHOD FOR LIKE SYSTEM
    private boolean isIdeaLiked(Long ideaId, Long userId, HttpServletRequest request){
        if (CookiesHandler.aquireCookie(userId.toString()+"donotlike"+ideaId.toString(), request)) {
            return true;
        }
        return false;
    }

    //SUPPORT METHOD FOR WATCH SYSTEM
    private boolean isWatched(Idea idea, Long userId, HttpServletRequest request){
        Object obj = request.getSession().getAttribute("donotwatch"+idea.getId());
        if ((null==obj) || (!userId.equals(idea.getAuthor().getId()))) return true;
        return false;
    }
}