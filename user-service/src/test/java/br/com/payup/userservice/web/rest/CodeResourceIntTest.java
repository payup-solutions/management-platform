package br.com.payup.userservice.web.rest;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.Instant;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;

import javax.persistence.EntityManager;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import br.com.payup.userservice.UserserviceApp;
import br.com.payup.userservice.domain.Code;
import br.com.payup.userservice.domain.Consumer;
import br.com.payup.userservice.service.CodeService;
import br.com.payup.userservice.service.dto.CodeDTO;
import br.com.payup.userservice.service.mapper.CodeMapper;
import br.com.payup.userservice.web.rest.errors.ExceptionTranslator;

/**
 * Test class for the CodeResource REST controller.
 *
 * @see CodeResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = UserserviceApp.class)
public class CodeResourceIntTest {

    private static final ZonedDateTime DEFAULT_ACTIVATION_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);

    private static final ZonedDateTime DEFAULT_EXPIRATION_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);

    private static final Boolean DEFAULT_ACTIVE = false;

    private static final String DEFAULT_VALUE = "AAAAAAAAAA";

    @Autowired
    private CodeMapper codeMapper;

    @Autowired
    private CodeService codeService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restCodeMockMvc;

    private Code code;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final CodeResource codeResource = new CodeResource(codeService);
        this.restCodeMockMvc = MockMvcBuilders.standaloneSetup(codeResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Code createEntity(EntityManager em) {
        Code code = new Code()
            .activationDate(DEFAULT_ACTIVATION_DATE)
            .expirationDate(DEFAULT_EXPIRATION_DATE)
            .active(DEFAULT_ACTIVE)
            .value(DEFAULT_VALUE);
        // Add required entity
        Consumer consumer = ConsumerResourceIntTest.createEntity(em);
        em.persist(consumer);
        em.flush();
        code.setConsumer(consumer);
        return code;
    }

    @Before
    public void initTest() {
        code = createEntity(em);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Code.class);
        Code code1 = new Code();
        code1.setId(1L);
        Code code2 = new Code();
        code2.setId(code1.getId());
        assertThat(code1).isEqualTo(code2);
        code2.setId(2L);
        assertThat(code1).isNotEqualTo(code2);
        code1.setId(null);
        assertThat(code1).isNotEqualTo(code2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(CodeDTO.class);
        CodeDTO codeDTO1 = new CodeDTO();
        codeDTO1.setId(1L);
        CodeDTO codeDTO2 = new CodeDTO();
        assertThat(codeDTO1).isNotEqualTo(codeDTO2);
        codeDTO2.setId(codeDTO1.getId());
        assertThat(codeDTO1).isEqualTo(codeDTO2);
        codeDTO2.setId(2L);
        assertThat(codeDTO1).isNotEqualTo(codeDTO2);
        codeDTO1.setId(null);
        assertThat(codeDTO1).isNotEqualTo(codeDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(codeMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(codeMapper.fromId(null)).isNull();
    }
}
