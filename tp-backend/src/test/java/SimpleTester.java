

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.google.gson.Gson;
import com.tp.proxy.cms.HitaoAppProxy;
import com.tp.proxy.cms.PositionProxy;
import com.tp.proxy.pay.PaymentInfoProxy;
import com.tp.service.cms.app.IAdvertiseAppService;
import com.tp.service.cms.app.IHaitaoAppService;
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath*:spring/spring-beans.xml" })
public class SimpleTester {


	@Autowired
	PositionProxy positionProxy;
	@Autowired
	IAdvertiseAppService advertiseAppService;
	@Autowired
	HitaoAppProxy hitaoAppProxy;
	@Autowired
	PaymentInfoProxy paymentInfoProxy;

	@Test
	public void simpleTest() {
		try {
			System.out.println(hitaoAppProxy.carouseAdvert());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testPay() {
		System.out.println(new Gson().toJson(paymentInfoProxy.refund("alipayDirect",2324343L)));
	}
	
}
