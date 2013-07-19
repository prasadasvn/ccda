package unit.com.ids.ccda.templates.header

import com.ids.ccda.Document
import com.ids.ccda.documents.ccd.uid.DocUid
import com.ids.ccda.templates.header.AuthorTemplate
import com.ids.ccda.templates.sections.body.ImmunizationsSectionTemplate
import org.junit.Before
import org.junit.Test

import static org.junit.Assert.assertTrue

public class AuthorTemplateTests {
    public static final MAP_KEY = "author"
    public static final TEST_MAP = [
    ]
    Document document
    DocUid docUid
    def template

    @Before
    void setUp() {
        docUid = new DocUid("DOC_UID")
        document = new Document( docUid, [author: [:]] )
    }

    @Test
    void testSomething() {
        template = new AuthorTemplate(document).builder
        document.display()
        assertTrue(true)
    }
}
