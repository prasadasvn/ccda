package com.ids.ccda.documents.ccd

import groovy.xml.MarkupBuilder

/**
 * Created with IntelliJ IDEA.
 * User: josh
 * Date: 3/15/13
 * Time: 10:20 AM
 * To change this template use File | Settings | File Templates.
 */
class CCDBody {

    def map
    MarkupBuilder builder

    CCDBody(builder, map = [:]) {
        this.builder = builder
        this.map = map
    }

    def generate(){

    }

}
