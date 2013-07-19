package unit.com.ids.ccda.templates

import com.ids.ccda.Document
import com.ids.ccda.documents.ccd.CCD
import com.ids.ccda.documents.ccd.uid.DocUid
import com.ids.ccda.templates.header.AuthorTemplate
import org.junit.Before
import org.junit.Test

import static org.junit.Assert.assertTrue

public class CCDTests {
    public static final MAP_KEY = "author"
    public static final TEST_MAP = [ : ]
    Document document
    DocUid docUid
    String patientId
    String tenantId
    Map map = [:]


    @Before
    void setUp() {
        patientId = "12345"
        tenantId = "777"
        map = TEST_MAP
    }

    @Test
    void testSomething() {
        def ccd = new CCD(tenantId, patientId, map)
        ccd.document.display()
        assertTrue(true)
    }
}
