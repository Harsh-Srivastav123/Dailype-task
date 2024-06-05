package com.dailype.dailypetask.services;

import com.dailype.dailypetask.dao.ManagerDao;
import com.dailype.dailypetask.entity.Manager;
import com.dailype.dailypetask.model.ManagerDto;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ManagerService {
    @Autowired
    ManagerDao managerDao;

    @Autowired
    ModelMapper modelMapper;

    public String createManager(ManagerDto managerDto) {
        Manager manager=modelMapper.map(managerDto, Manager.class);
        String manager_id=managerDao.save(manager).getManagerId();
        log.info("manager created successfully  "+manager_id);
        return  manager_id;
    }
}
