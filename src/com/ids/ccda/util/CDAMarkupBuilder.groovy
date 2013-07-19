package com.ids.ccda.util

import groovy.xml.MarkupBuilder
import org.codehaus.groovy.runtime.InvokerHelper

class CDAMarkupBuilder extends MarkupBuilder {

    public static final DEFAULT_NULL_FLAVOR = "NI"
    public static final DEFAULT_NULL_MAP = [nullFlavor:DEFAULT_NULL_FLAVOR ]

    CDAMarkupBuilder() {
    }

    CDAMarkupBuilder(PrintWriter pw) {
        super(pw)
    }

    CDAMarkupBuilder(Writer writer) {
        super(writer)
    }

    CDAMarkupBuilder(IndentPrinter out) {
        super(out)
    }

    public Object invokeMethod(String methodName, Object args){
        println args
        if (args.size() > 0) {
          args = argsHandler(args)
        }
        super.invokeMethod(methodName, args)
    }

    private Object argsHandler (Object args){
        List list = InvokerHelper.asList(args);
        Object obj = list.get(0);
        if(obj instanceof LinkedHashMap){
          args = linkedHashMapHandler(args)
        }
        return args
    }

    private LinkedHashMap linkedHashMapHandler (LinkedHashMap map = [:]){
        /*
        Assumption
       LinkedHashMap has order, so should be able to rely on first KV passed
       1)If map contains a KV with K of nullCheck then eval nullCheck V is blank
           2)If blank,  use nullFlavor if specified; or 3)default null flavor if not
       4)Else if  first KV's value is blank then
           5)If blank, then use nullFlavor if specified, or 6)default null flavor if not
       7)NullFlavor N/A - neither the nullCheck V or first KV are blank
         */
        if( (map.containsKey("nullCheck") && !map.nullCheck?.value ) ||
                map.isEmpty() ||
                !map.entrySet().iterator().next().value ){
            map = returnNullFlavor(map)
        }
        return map
    }

    private LinkedHashMap returnNullFlavor(LinkedHashMap map){
        if(map.nullFlavor){
            map = [nullFlavor: map.nullFlavor]
        } else {
            map = [nullFlavor:"NI"]
        }
        return map
    }
}
