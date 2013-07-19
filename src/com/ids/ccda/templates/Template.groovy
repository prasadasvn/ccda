package com.ids.ccda.templates

import com.ids.ccda.Generatable
import com.ids.ccda.documents.ccd.uid.DocUid
import groovy.xml.MarkupBuilder

public interface Template extends Generatable {
       def TEST_MAP
       def ATTRS
       def map
       DocUid docUid
       MarkupBuilder builder
}