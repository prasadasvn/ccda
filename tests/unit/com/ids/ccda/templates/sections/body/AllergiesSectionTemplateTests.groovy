package unit.com.ids.ccda.templates.sections.body

import com.ids.ccda.Document
import com.ids.ccda.documents.ccd.uid.DocUid
import com.ids.ccda.templates.sections.body.AllergiesSectionTemplate
import org.junit.Before;
import org.junit.Test
import static org.junit.Assert.*

public class AllergiesSectionTemplateTests {
    public static final TEST_MAP =  [  //MU
            "1325890325": [  code: "573521", name: "Albuterol",  routeCode: "38216", routeName: "RESPIRATORY (INHALATION)",
                    startDate: new Date().format("yyyyMMdd"), stopDate: new Date().format("yyyyMMdd"),
                    dosageQuantity: "0.09" ,dosageUnit: "MG/ACTUAT",
                    instructions: "2 puffs once"]
    ]
    public static final MAP_KEY = "allergies"

    Document document
    DocUid docUid
    def template

    @Before
    void setUp() {
        docUid = new DocUid("DOC_UID")
        document = new Document( docUid, [ allergies:TEST_MAP])

    }

    @Test
    void testSomething() {
        template = new AllergiesSectionTemplate(document).builder
        document.display()
        assertTrue(true)
    }
}
