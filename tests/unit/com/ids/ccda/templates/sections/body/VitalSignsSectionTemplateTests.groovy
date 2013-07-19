package unit.com.ids.ccda.templates.sections.body

import com.ids.ccda.Document
import com.ids.ccda.documents.ccd.uid.DocUid
import com.ids.ccda.templates.sections.body.ImmunizationsSectionTemplate
import com.ids.ccda.templates.sections.body.VitalSignsSectionTemplate
import org.junit.Before
import org.junit.Test

import static org.junit.Assert.assertTrue

public class VitalSignsSectionTemplateTests {
    public static final MAP_KEY = "vitalSigns"
    public static final TEST_MAP = [
            "1289054":[  date: "20081101",
                    heightMeasurement: "177", heightUnits: "cm",
                    weightMeasurement: "86", weightUnits: "kg",
                    systolicMeasurement: "132", systolicUnits: "mmHg",
                    diastolicMeasurement: "86", diastolicUnits: "mmHg",
                    bmiMeasurement: "27.45", bmiUnits: ""
            ],
            "2362342":[  date: "20120806",
                    heightMeasurement: "177", heightUnits: "cm",
                    weightMeasurement: "82", weightUnits: "kg",
                    systolicMeasurement: "145", systolicUnits: "mmHg",
                    diastolicMeasurement: "88", diastolicUnits: "mmHg",
                    bmiMeasurement: "28.09", bmiUnits: ""
            ]
    ]
    Document document
    DocUid docUid
    def template

    @Before
    void setUp() {
        docUid = new DocUid("DOC_UID")
        document = new Document( docUid, [vitalSigns:TEST_MAP])
    }

    @Test
    void testSomething() {
        template = new VitalSignsSectionTemplate(document).builder
        document.display()
        assertTrue(true)
    }
}
