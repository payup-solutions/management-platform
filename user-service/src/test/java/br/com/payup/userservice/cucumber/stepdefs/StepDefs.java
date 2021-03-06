package br.com.payup.userservice.cucumber.stepdefs;

import br.com.payup.userservice.UserserviceApp;

import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.ResultActions;

import org.springframework.boot.test.context.SpringBootTest;

@WebAppConfiguration
@SpringBootTest
@ContextConfiguration(classes = UserserviceApp.class)
public abstract class StepDefs {

    protected ResultActions actions;

}
