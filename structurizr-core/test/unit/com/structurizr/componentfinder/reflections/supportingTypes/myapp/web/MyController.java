package com.structurizr.componentfinder.reflections.supportingTypes.myapp.web;

import com.structurizr.annotation.Component;
import com.structurizr.componentfinder.reflections.supportingTypes.myapp.AbstractComponent;
import com.structurizr.componentfinder.reflections.supportingTypes.myapp.data.MyRepository;

@Component
class MyController extends AbstractComponent {

    private MyRepository myRepository;

}
