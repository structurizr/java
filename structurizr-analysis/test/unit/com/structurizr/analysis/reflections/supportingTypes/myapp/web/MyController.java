package com.structurizr.analysis.reflections.supportingTypes.myapp.web;

import com.structurizr.analysis.reflections.supportingTypes.myapp.AbstractComponent;
import com.structurizr.analysis.reflections.supportingTypes.myapp.data.MyRepository;
import com.structurizr.annotation.Component;

@Component
class MyController extends AbstractComponent {

    private MyRepository myRepository;

}
