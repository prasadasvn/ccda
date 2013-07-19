package unit.com.ids.ccda.util

import com.ids.ccda.Document
import com.ids.ccda.documents.ccd.CCD
import com.ids.ccda.documents.ccd.uid.DocUid
import com.ids.ccda.util.IDSMarkupBuilder
import groovy.xml.MarkupBuilder
import org.junit.Before
import org.junit.Test

import static org.junit.Assert.assertTrue

public class IDSMarkupBuilderTests {


    public Map map = [:]
    public StringWriter writer



    @Before
    void setUp() {
      writer = new StringWriter()
    }

    @Test
    void testSomething() {
        IDSMarkupBuilder builder = new IDSMarkupBuilder(writer)
        builder.blah(a:1,b:2,c:3)
        assertTrue(true)
    }
}
