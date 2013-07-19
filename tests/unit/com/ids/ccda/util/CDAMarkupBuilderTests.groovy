package unit.com.ids.ccda.util

import com.ids.ccda.util.CDAMarkupBuilder
import com.ids.ccda.util.IDSMarkupBuilder
import junit.framework.Assert
import org.junit.Before
import org.junit.Test

import static org.junit.Assert.assertEquals
import static org.junit.Assert.assertTrue

public class CDAMarkupBuilderTests {


    public Map map = [:]
    public StringWriter writer



    @Before
    void setUp() {
      writer = new StringWriter()
    }

    @Test
    void testSomething() {
        CDAMarkupBuilder builder = new CDAMarkupBuilder(writer)
        builder.blah()
        assertTrue(true)
    }
    // Assumption -- only pertains when args passed are LinkedHashMap
    // SHOULDS

    // builder.blah(a:null or a:"") should result in  <blah nullFlavor="NI"/>
    // builder.blah(a:null or a:"", nullFlavor: "BLERG") should result in  <blah nullFlavor="BLERG"/>
    // SHOULDNTS
    // builder.blah(a:"not null") should result in <blah a="not null"/>
    // builder.blah(a:"not null", b:null) should result in <blah a="not null" b="" />

   //MAYBE
    // builder.blah() should result in <blah nullFlavor="NI"/>

/*    @Test
    void testFirstAttributeNullShouldResultInNullFlavor(){
        // builder.blah() should result in <blah nullFlavor="NI"/>
        CDAMarkupBuilder builder = new CDAMarkupBuilder(writer)
        builder.blah()
        assertTrue(true)
    }*/


   @Test void testReturnDefaultNullMap(){
       CDAMarkupBuilder builder = new CDAMarkupBuilder(writer)
       def mapResult = builder.returnNullFlavor(map)
       assertEquals(CDAMarkupBuilder.DEFAULT_NULL_MAP,mapResult)

   }

    @Test void testReturnSpecifiedNullFlavor(){
        map.nullFlavor = "NA"
        CDAMarkupBuilder builder = new CDAMarkupBuilder(writer)
        def mapResult = builder.returnNullFlavor(map)
        assertEquals([nullFlavor:"NA"], mapResult)
    }

    @Test void testLinkedHashMapHandlerReturnsNullFlavorForMapWithNullCheck(){
        map.nullCheck = null
        CDAMarkupBuilder builder = new CDAMarkupBuilder(writer)
        def mapResult = builder.returnNullFlavor(map)
        assertEquals(CDAMarkupBuilder.DEFAULT_NULL_MAP, mapResult)
        map.nullCheck = ""
        mapResult = builder.returnNullFlavor(map)
        assertEquals(CDAMarkupBuilder.DEFAULT_NULL_MAP, mapResult)
        map = [a:"blah", nullCheck:null]
        mapResult = builder.returnNullFlavor(map)
        assertEquals(CDAMarkupBuilder.DEFAULT_NULL_MAP, mapResult)
    }


    @Test void testLinkedHashMapHandlerReturnsNullFlavorFoMapWithNoNullCheck(){
        CDAMarkupBuilder builder = new CDAMarkupBuilder(writer)
        def mapResult = builder.returnNullFlavor(map)
        assertEquals(CDAMarkupBuilder.DEFAULT_NULL_MAP, mapResult)
        map.shouldBeNull = null
        mapResult = builder.returnNullFlavor(map)
        assertEquals(CDAMarkupBuilder.DEFAULT_NULL_MAP, mapResult)
        map.shouldBeNull = ""
        mapResult = builder.returnNullFlavor(map)
        assertEquals(CDAMarkupBuilder.DEFAULT_NULL_MAP, mapResult)
    }

    @Test void testArgsHandler(){
        //TEST pass through non-LinkedHashMap objects
        CDAMarkupBuilder builder = new CDAMarkupBuilder(writer)
        def object= new Object()
        def result  = builder.argsHandler(object)
        assertEquals(object,result)
        //TEST handles LinkedHashMap appropriately
        def map = [:]
        assertTrue(map instanceof LinkedHashMap)
        result =  builder.argsHandler(map)
        assertTrue(result instanceof LinkedHashMap)
        assertEquals(CDAMarkupBuilder.DEFAULT_NULL_MAP,result)
        //TEST pass through map successfully
        map = [a:1]
        result =  builder.argsHandler(map)
        assertEquals(map,result)
        map = [a:1,b:2,c:3]
        result =  builder.argsHandler(map)
        assertEquals(map,result)

    }



}
