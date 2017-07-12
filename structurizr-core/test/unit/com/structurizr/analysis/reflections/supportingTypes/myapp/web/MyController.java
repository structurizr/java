package com.structurizr.analysis.reflections.supportingTypes.myapp.web;

import com.structurizr.annotation.Component;
import com.structurizr.analysis.reflections.supportingTypes.myapp.AbstractComponent;
import com.structurizr.analysis.reflections.supportingTypes.myapp.data.MyRepository;

@Component
class MyController extends AbstractComponent {

    private MyRepository myRepository;

}
