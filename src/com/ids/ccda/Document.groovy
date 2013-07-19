package com.ids.ccda


import com.ids.ccda.documents.ccd.uid.DocUid
import groovy.xml.MarkupBuilder

class Document {
    public DocUid docUid
    public MarkupBuilder builder
    public Map map
    public StringWriter writer

    Document( DocUid docUid, Map map = [:]) {
        this.writer = new StringWriter()
        this.builder = new MarkupBuilder(writer)
        this.docUid = docUid
        this.map = map
    }

    String toString(){
      return writer
    }

    def display(){
        println writer
    }

}
