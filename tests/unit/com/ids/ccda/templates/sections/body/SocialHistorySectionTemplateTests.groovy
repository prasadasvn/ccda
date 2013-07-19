package unit.com.ids.ccda.templates.sections.body

import com.ids.ccda.Document
import com.ids.ccda.documents.ccd.uid.DocUid
import com.ids.ccda.templates.sections.body.AllergiesSectionTemplate
import com.ids.ccda.templates.sections.body.SocialHistorySectionTemplate
import org.junit.Before
import org.junit.Test

import static org.junit.Assert.assertTrue

public class SocialHistorySectionTemplateTests {
    public static final MAP_KEY = "socialHistory"
    public static final TEST_MAP =  [
            "123512343":[  name:  "Smoking Status",startDate:  "1992", endDate:  "",
                    elementCode:"449868002", elementDisplayName: "Current every day smoker"
            ]
    ]

    Document document
    DocUid docUid
    def template

    @Before
    void setUp() {
        docUid = new DocUid("DOC_UID")
        document = new Document( docUid, [ socialHistoryElements:TEST_MAP])

    }

    @Test
    void testSomething() {
        template = new SocialHistorySectionTemplate(document).builder
        document.display()
        assertTrue(true)
    }
}
