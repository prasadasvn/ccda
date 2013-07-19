package unit.com.ids.ccda.templates.sections.body

import com.ids.ccda.Document
import com.ids.ccda.documents.ccd.uid.DocUid
import com.ids.ccda.templates.sections.body.AllergiesSectionTemplate
import com.ids.ccda.templates.sections.body.ResultsSectionTemplate
import org.junit.Before
import org.junit.Test

import static org.junit.Assert.assertTrue

public class ResultsSectionTemplateTests {
    public static final TEST_MAP = [
            "2134567": [ resultCode: "30313-1", resultName:  "HGB", effectiveDate: new Date().format("yyyyMMddHHmmmSS"),
                    observationLoincCode: "30313-1", observationName: "HGB", observationValue: "14.2", observationUnit: "" ],
            "2468909": [ resultCode: "20570-8", resultName:  "HCT", effectiveDate: new Date().format("yyyyMMddHHmmmSS"),
                    observationLoincCode: "20570-8", observationName: "HCT", observationValue: "45%", observationUnit: "" ],
            "4576890": [ resultCode: "33765-9", resultName:  "WBC", effectiveDate: new Date().format("yyyyMMddHHmmmSS"),
                    observationLoincCode: "33765-9", observationName: "WBC", observationValue: "7.6", observationUnit: "10^3/ul" ],
            "7890454": [ resultCode: "26515-7", resultName:  "PLT", effectiveDate: new Date().format("yyyyMMddHHmmmSS"),
                    observationLoincCode: "26515-7", observationName: "PLT", observationValue: "220", observationUnit: "10^3/ul" ]
    ]
    public static final MAP_KEY = "results"

    Document document
    DocUid docUid
    def template

    @Before
    void setUp() {
        docUid = new DocUid("DOC_UID")
        document = new Document( docUid, [ results:TEST_MAP])

    }

    @Test
    void testSomething() {
        template = new ResultsSectionTemplate(document).builder
        document.display()
        assertTrue(true)
    }
}
