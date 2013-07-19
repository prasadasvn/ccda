package unit.com.ids.ccda.templates.sections.body

import com.ids.ccda.Document
import com.ids.ccda.documents.ccd.uid.DocUid
import com.ids.ccda.templates.sections.body.ImmunizationsSectionTemplate
import com.ids.ccda.templates.sections.body.MedicationsAdministeredSectionTemplate
import org.junit.Before
import org.junit.Test

import static org.junit.Assert.assertTrue

public class MedicationsAdministeredSectionTemplateTests {
    public static final TEST_MAP = [
            "32151234":[  code: "88", name: "Influenza virus vaccine", date: "20051101"],
            "23652334":[  code: "88", name: "Influenza virus vaccine", date: "20060910"],
            "25475426":[  code: "09", name: "Tetanus-diphtheria adult", date: "20070104"],
            "98775442":[  code: "33", name: "Pneumococcal polysaccharide", date: "20120806"]
    ]
    public static final MAP_KEY = "medicationsAdministered"
    Document document
    DocUid docUid
    def template

    @Before
    void setUp() {
        docUid = new DocUid("DOC_UID")
        document = new Document( docUid, [medicationsAdministered:TEST_MAP])
    }

    @Test
    void testSomething() {
        template = new MedicationsAdministeredSectionTemplate(document).builder
        document.display()
        assertTrue(true)
    }
}
