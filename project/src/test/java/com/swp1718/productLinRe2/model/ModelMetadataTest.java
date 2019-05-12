package com.swp1718.productLinRe2.model;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Unittest of the pair-model metadata
 * 
 * @author Tim Gugel
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class ModelMetadataTest {

	/**
	 * Testing the constructor
	 * 
	 * @throws Exception
	 */
	@Test
	public void constructorTest() throws Exception {
		String label = "Label";
		String content = "Content is more than one word";
		Metadata meta = new Metadata(label, content);
		assertThat(meta.getLabel()).isEqualTo(label);
		assertThat(meta.getContent()).isEqualTo(content);
	}

	/**
	 * Testing the getter and setter
	 * 
	 * @throws Exception
	 */
	@Test
	public void testMetadataCorefeatures() throws Exception {
		String label = "Label";
		String content = "Content is more than one word";
		String label_n = "New Label";
		String content_n = "wan839 893 883bj 3";
		Metadata meta = new Metadata(label, content);
		// Testing exist
		assertThat(meta).isNotNull();
		// Testing getter
		assertThat(meta.getLabel()).isEqualTo(label);
		assertThat(meta.getContent()).isEqualTo(content);
		// Testing setter
		meta.setLabel(label_n);
		meta.setContent(content_n);
		assertThat(meta.getLabel()).isEqualTo(label_n);
		assertThat(meta.getContent()).isEqualTo(content_n);
		// Testing equals
		assertThat(meta.equals(meta)).isTrue();
		assertThat(meta.equals(new Metadata("Some new", "Some blue"))).isFalse();
	}
}