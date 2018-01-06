package com.hjrh.sso.web.action;

import java.io.UnsupportedEncodingException;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.hjrh.sso.core.key.KeyService;
import com.hjrh.sso.core.key.SsoKey;
import com.hjrh.sso.web.action.KeyAction;

public class KeyActionTest {
	
	private KeyAction keyAction = new KeyAction();
	
	@Before
	public void setUp() throws Exception {
		keyAction = new KeyAction();
	}

	@After
	public void tearDown() throws Exception {
	}
	
	@Test
	public void testFetchKey() throws UnsupportedEncodingException {
		KeyService keyService = Mockito.mock(KeyService.class);
		SsoKey ssoKey = Mockito.mock(SsoKey.class);
		Mockito.when(keyService.findKeyByAppId(Mockito.anyString())).thenReturn(ssoKey);
		keyAction.setKeyService(keyService);
	}

}
