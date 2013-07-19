package unit.com.ids.ccda.templates.header

import com.ids.ccda.Document
import com.ids.ccda.documents.ccd.uid.DocUid
import com.ids.ccda.templates.header.RecordTargetTemplate
import com.ids.ccda.templates.header.ServiceEventTemplate
import org.junit.Before
import org.junit.Test

import static org.junit.Assert.assertTrue

public class ServiceEventTemplateTests {
    public static final MAP_KEY = "encounters"
    public static final TEST_MAP = [ [ startDate: "20120117142500", endDate:"20120117144500", primaryDiagnosisCode:"212.01", primaryDiagnosisDisplayName: "A problem",
            attendingProviderNpi: "12345678", attendingProviderLastName: "Doe", attendingProviderFirstName: "Joe" ] ]
    Document document
    DocUid docUid
    def template

    @Before
    void setUp() {
        docUid = new DocUid("DOC_UID")
        document = new Document( docUid, [encounters:TEST_MAP])
    }

    @Test
    void testSomething() {
        template = new ServiceEventTemplate(document).builder
        document.display()
        assertTrue(true)
    }
}
