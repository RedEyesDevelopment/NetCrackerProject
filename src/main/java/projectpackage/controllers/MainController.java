package projectpackage.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import projectpackage.model.Model;
import projectpackage.service.ModelService;

/**
 * Created by Gvozd on 30.12.2016.
 */
@RestController
public class MainController {

    @Autowired
    ModelService modelService;

    private final String PREFIX="/fuck/";

    @ResponseBody
    @RequestMapping(value = PREFIX + "gimme")
    public ResponseEntity getModelViaAjax() {
        Model model = modelService.getModel(1);
        ResponseEntity result = new ResponseEntity(model, HttpStatus.OK);
        return result;
    }

    @ResponseBody
    @RequestMapping(value = PREFIX + "gimme", params = {"model_id"})
    public ResponseEntity getModelWithIdViaAjax(@RequestParam(value = "model_id") Integer model_id) {
            Model model = modelService.getModel(model_id);
            if (null!=model) {
                ResponseEntity result = new ResponseEntity(model, HttpStatus.OK);
                return result;
            } else return new ResponseEntity("No model with ID="+model_id, HttpStatus.BAD_REQUEST);
    }
}
