package br.com.payup.clientservice.cucumber.stepdefs;

import br.com.payup.clientservice.ClientserviceApp;

import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.ResultActions;

import org.springframework.boot.test.context.SpringBootTest;

@WebAppConfiguration
@SpringBootTest
@ContextConfiguration(classes = ClientserviceApp.class)
public abstract class StepDefs {

    protected ResultActions actions;

}
