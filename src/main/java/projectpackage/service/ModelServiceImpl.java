package projectpackage.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import projectpackage.model.Model;
import projectpackage.repository.ModelRepository;

/**
 * Created by Lenovo on 09.04.2017.
 */
@Service
public class ModelServiceImpl implements ModelService{

    @Autowired
    ModelRepository modelRepository;

    @Override
    public Model getModel(int id) {
        return modelRepository.getModel(id);
    }
}
