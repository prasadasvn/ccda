package unit.com.ids.ccda.templates.header

import com.ids.ccda.Document
import com.ids.ccda.documents.ccd.uid.DocUid
import com.ids.ccda.templates.header.CustodianTemplate
import com.ids.ccda.templates.sections.body.ImmunizationsSectionTemplate
import org.junit.Before
import org.junit.Test

import static org.junit.Assert.assertTrue

public class CustodianTemplateTests {
    public static final MAP_KEY = "custodian"
    public static final TEST_MAP = [ practiceName: "Inspiration Eye Clinic", npi: "12345678", phone: "(214) 777-3344",
            addressLine1:"101 Main St", addressLine2: "Ste 300", city: "Temple", state: "TX", zipCode: "76502"  ]
    Document document
    DocUid docUid
    def template

    @Before
    void setUp() {
        docUid = new DocUid("DOC_UID")
        document = new Document( docUid, [custodian:TEST_MAP])
    }

    @Test
    void testSomething() {
        template = new CustodianTemplate(document).builder
        document.display()
        assertTrue(true)
    }
}
