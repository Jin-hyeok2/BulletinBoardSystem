package com.personal.bulletinboardsystem;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class BulletinboardsystemApplicationTests {
/*
 * assertSame(a, b) 객체 a, b가 같은 객체임을 확인
 * assertEquals(a, b) 객체 a, b가 일치함을 확인(안에 값을 같은지 확인)
 * 
 */
	@Test
	void equalTest() {
		assertEquals(1, 1);
	}

	@Test
	void nullTest() {
		assertNull(null);
	}

	@Test
	void trueTest() {
	}

}
