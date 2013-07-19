package unit.com.ids.ccda.templates.sections.body

import com.ids.ccda.Document
import com.ids.ccda.documents.ccd.uid.DocUid
import com.ids.ccda.templates.sections.body.AllergiesSectionTemplate
import com.ids.ccda.templates.sections.body.ProblemsSectionTemplate
import org.junit.Before
import org.junit.Test

import static org.junit.Assert.assertTrue

public class ProblemsSectionTemplateTests {
    public static final MAP_KEY = "problems"
    public static final TEST_MAP = [
            "123512354":[  code:  "123456", name:  "Problem Name1",  statusCode: "55561003", statusName:"Active",
                    activeDates: [ startDate: "20090211", endDate: null],  observedDates: [ startDate: "20120114", endDate: null]
            ],
            "262354322":[    code:  "765432", name:  "Problem Name2",  statusCode: "55561003", statusName:"Active",
                    activeDates: [ startDate: "20120114", endDate: null] ,  observedDates: [ startDate: "20120114", endDate: null]

            ]
    ]
    Document document
    DocUid docUid
    def template

    @Before
    void setUp() {
        docUid = new DocUid("DOC_UID")
        document = new Document( docUid, [ problems:TEST_MAP])

    }

    @Test
    void testSomething() {
        template = new ProblemsSectionTemplate(document).builder
        document.display()
        assertTrue(true)
    }
}
