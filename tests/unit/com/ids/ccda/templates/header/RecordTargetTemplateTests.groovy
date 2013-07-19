package unit.com.ids.ccda.templates.header

import com.ids.ccda.Document
import com.ids.ccda.documents.ccd.uid.DocUid
import com.ids.ccda.templates.header.CustodianTemplate
import com.ids.ccda.templates.header.RecordTargetTemplate
import org.junit.Before
import org.junit.Test

import static org.junit.Assert.assertTrue

public class RecordTargetTemplateTests {
    public static final MAP_KEY = "patient"
    public static final TEST_MAP = [id: "1234", firstName: "John", lastName: "Doe", middleName: "Q",  dateOfBirth: "19690413",
            addressLine1: "123 Main St", addressLine2: "Apt 24", city: "Temple",  state: "TX", zipCode: "76502",
            homePhone: "(214) 123-4567", workPhone: "(214) 789-0012", email: "johndoe@testme.com",
            genderCode: "M", genderDisplayName: "Male", maritalStatusCode: "M", maritalStatusDisplayName: "Married",
            raceCode: "2106-3", raceDisplayName: "White", ethnicityCode:"2186-5", ethnicityDisplayName:"Not Hispanic or Latino"
    ]
    Document document
    DocUid docUid
    def template

    @Before
    void setUp() {
        docUid = new DocUid("DOC_UID")
        document = new Document( docUid, [patient:TEST_MAP])
    }

    @Test
    void testSomething() {
        template = new RecordTargetTemplate(document).builder
        document.display()
        assertTrue(true)
    }
}
