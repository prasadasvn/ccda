package unit.com.ids.ccda.templates.header

import com.ids.ccda.Document
import com.ids.ccda.documents.ccd.uid.DocUid
import com.ids.ccda.templates.header.ParticipantsTemplate
import com.ids.ccda.templates.sections.body.ImmunizationsSectionTemplate
import org.junit.Before
import org.junit.Test

import static org.junit.Assert.assertTrue

public class ParticipantsTemplateTests {
    public static final MAP_KEY = "participants"
    public static final TEST_MAP = [
            [relationshipCode: "PRN", relationshipDisplayName: "Parent", firstName: "Joan", lastName: "Doe"],
            [relationshipCode: "SIB", relationshipDisplayName: "Sibling", firstName: "Elroy", lastName: "Doe"]
    ]
    Document document
    DocUid docUid
    def template

    @Before
    void setUp() {
        docUid = new DocUid("DOC_UID")
        document = new Document( docUid, [participants:TEST_MAP])
    }

    @Test
    void testSomething() {
        template = new ParticipantsTemplate(document).builder
        document.display()
        assertTrue(true)
    }
}
