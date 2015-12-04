// Fichier TestLivreFacade.java
// Auteur : Dragons Vicieux
// Date de création : 2015-12-04

package test.collegeahuntsic.bibliothequeBackEnd.facade;

import java.sql.Timestamp;
import java.util.List;
import ca.qc.collegeahuntsic.bibliothequeBackEnd.dto.LivreDTO;
import ca.qc.collegeahuntsic.bibliothequeBackEnd.exception.dao.InvalidHibernateSessionException;
import ca.qc.collegeahuntsic.bibliothequeBackEnd.exception.dao.InvalidPrimaryKeyException;
import ca.qc.collegeahuntsic.bibliothequeBackEnd.exception.dao.InvalidSortByPropertyException;
import ca.qc.collegeahuntsic.bibliothequeBackEnd.exception.dto.InvalidDTOException;
import ca.qc.collegeahuntsic.bibliothequeBackEnd.exception.facade.FacadeException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import test.collegeahuntsic.bibliothequeBackEnd.exception.TestCaseFailedException;

/**
 * Test case for {@link test.collegeahuntsic.bibliothequeBackEnd.facade.TestCase}.
 *
 * @author Dragons Vicieux
 */
public class TestLivreFacade extends TestCase {

    private static final Log LOGGER = LogFactory.getLog(TestLivreFacade.class);
    
    private static final String TEST_CASE_TITLE = "Livre facade test case"; //$NON-NLS-1$
    
    private static final String TITRE = "Titre "; //$NON-NLS-1$
    
    private static final String AUTEUR = "Auteur "; //$NON-NLS-1$
    
    private static int sequence;
    
    /**
     * Default constructor.
     *
     * @param name The name of the test case
     * @throws TestCaseFailedException If an error occurs
     */
    public TestLivreFacade(String name) throws TestCaseFailedException{
        super(name);
    }
    
    static{
        TestLivreFacade.sequence = 1;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    protected void setUp() throws Exception{
        super.setUp();
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    protected void tearDown() throws Exception{
        super.tearDown();
    }
    
    /**
     * Test unitaire AcquerirLivre
     *
     * @throws TestCaseFailedException S'il y a une erreur
     */
    public void testAcquerirLivre() throws TestCaseFailedException{
        try {
            beginTransaction();
            final LivreDTO livreDTO = new LivreDTO();
            livreDTO.setTitre(TestLivreFacade.TITRE + TestLivreFacade.sequence);
            livreDTO.setAuteur(TestLivreFacade.AUTEUR + TestLivreFacade..sequence);
            livreDTO.setDateAcquisition(new Timestamp(System.currentTimeMillis()));
            TestLivreFacade.sequence = TestLivreFacade.sequence + 1;
            getLivreFacade().acquerirLivre(getSession(), livreDTO);
            commitTransaction();
        } catch(InvalidHibernateSessionException | InvalidDTOException | FacadeException exception) {
            try {
                rollbackTransaction();
            } catch(TestCaseFailedException testCaseFailedException) {
                TestLivreFacade.LOGGER.error(testCaseFailedException);
            }
            TestLivreFacade.LOGGER.error(exception);
        }
    }
    
    /**
     * Test if {@link ca.qc.collegeahuntsic.bibliothequeBackEnd.facade.implementations.LivreFacade}
     *
     * @throws TestCaseFailedException S'il y a une erreur
     */
    public void testGetAllLivres() throws TestCaseFailedException{
        try {
            testAcquerirLivre();
            beginTransaction();
            final List<LivreDTO> livres = getLivreFacade().getAllLivres(getSession(), LivreDTO.TITRE_COLUMN_NAME);
            assertFalse(livres.isEmpty());
            for(LivreDTO livreDTO : livres){
                assertNotNull(livreDTO);
                assertNotNull(livreDTO.getIdLivre());
                assertNotNull(livreDTO.getTitre());
                assertNotNull(livreDTO.getAuteur());
                assertNotNull(livreDTO.getDateAcquisition());
                final String idLivre = livreDTO.getIdLivre();
                final String titre = livreDTO.getTitre();
                final String auteur = livreDTO.getAuteur();
                final Timestamp dateAcquisition = livreDTO.getDateAcquisition();
                final LivreDTO unLivreDTO = getLivreFacade().getLivre(getSession(), idLivre);
                assertEquals(idLivre, unLivreDTO.getIdLivre());
                assertEquals(titre, unLivreDTO.getTitre());
                assertEquals(auteur, unLivreDTO.getAuteur());
                assertEquals(dateAcquisition, unLivreDTO.getDateAcquisition());
            }           
            commitTransaction();
        } catch(InvalidHibernateSessionException | FacadeException | InvalidPrimaryKeyException | InvalidSortByPropertyException exception) {
            try {
                rollbackTransaction();
            } catch(TestCaseFailedException testCaseFailedException) {
                TestLivreFacade.LOGGER.error(testCaseFailedException);
            }
            TestLivreFacade.LOGGER.error(exception);
        }
    }
    
    

}
