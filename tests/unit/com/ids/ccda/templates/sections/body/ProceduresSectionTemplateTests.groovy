package unit.com.ids.ccda.templates.sections.body

import com.ids.ccda.Document
import com.ids.ccda.documents.ccd.uid.DocUid
import com.ids.ccda.templates.sections.body.AllergiesSectionTemplate
import com.ids.ccda.templates.sections.body.ProceduresSectionTemplate
import org.junit.Before
import org.junit.Test

import static org.junit.Assert.assertTrue

public class ProceduresSectionTemplateTests {
    public static final TEST_MAP =  [ //MU
            "235231122365": [ code: "168731009", name: "Chest X-Ray, PA and Lateral Views", date:  "20120806",
                    bodySiteCode: "82094008", bodySiteName:"Lower Respiratory Tract Structure",   performer: [ firstName:  "John", lastName:  "House", npi: "123456789",
                    telecom: "2541234567", addressLine1: "123 Main St",
                    addressLine2: "Apt 456", city:  "Temple",  state: "TX", zipCode:  "76002" ] ]

    ]
    public static final MAP_KEY = "procedures"

    Document document
    DocUid docUid
    def template

    @Before
    void setUp() {
        docUid = new DocUid("DOC_UID")
        document = new Document( docUid, [ procedures:TEST_MAP])
    }

    @Test
    void testSomething() {
        template = new ProceduresSectionTemplate(document).builder
        document.display()
        assertTrue(true)
    }
}
