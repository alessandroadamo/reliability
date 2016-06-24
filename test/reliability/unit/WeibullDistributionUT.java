package reliability.unit;

import static org.junit.Assert.*;

import org.junit.Test;

import reliability.WeibullDistribution;

public class WeibullDistributionUT {

	@Test
	public void testPdfDouble() {

		WeibullDistribution dist = new WeibullDistribution(5.0, 15.0);

		if (Math.abs(dist.pdf(2.0) - 1.053453549005620028842E-4) >= 1e-6)
			fail("Value non exact!");

		if (Math.abs(dist.pdf(2.5) - 2.571685718738904623074E-4) >= 1e-6)
			fail("Value non exact!");

	}

	@Test
	public void testCdfDouble() {

		WeibullDistribution dist = new WeibullDistribution(12.0, 30.0);

		if (Math.abs(dist.cdf(22.5) - 0.03117991199580326716834) >= 1e-6)
			fail("Value non exact!");

		if (Math.abs(dist.cdf(36.0) - 0.9998657894131925274743) >= 1e-6)
			fail("Value non exact!");

	}

	@SuppressWarnings("unused")
	@Test
	public void testWeibullDistribution() {

		boolean thrown1 = true;
		boolean thrown2 = false;
		boolean thrown3 = false;

		try {
			WeibullDistribution dist1 = new WeibullDistribution(1.0, 1.0);
		} catch (IllegalArgumentException ex) {
			thrown1 = false;
		}

		try {
			WeibullDistribution dist2 = new WeibullDistribution(0.0, 1.0);
		} catch (IllegalArgumentException ex) {
			thrown2 = true;
		}

		try {
			WeibullDistribution dist3 = new WeibullDistribution(1.0, 0.0);
		} catch (IllegalArgumentException ex) {
			thrown3 = true;
		}

		assertTrue(thrown1);
		assertTrue(thrown2);
		assertTrue(thrown3);

	}

	@Test
	public void testPdfDoubleArray() {

		double x[] = { 0.07797900748818658766462, 0.09838504246724308999983,
				0.1153295983190182380462 };

		WeibullDistribution dist = new WeibullDistribution(5.0, 15.0);
		double vals[] = dist.pdf(new double[] { 11, 12, 13 });

		for (int i = 0; i < x.length; i++)
			if (Math.abs(vals[i] - x[i]) >= 1e-6)
				fail("Error!");

	}

	@Test
	public void testCdfDoubleArray() {

		double x[] = { 0.1911029463651159072765, 0.2794064272418718994153,
				0.3867272593851442560394 };

		WeibullDistribution dist = new WeibullDistribution(5.0, 15.0);
		double vals[] = dist.cdf(new double[] { 11, 12, 13 });

		for (int i = 0; i < x.length; i++)
			if (Math.abs(vals[i] - x[i]) >= 1e-6)
				fail("Error!");

	}

	@Test
	public void testReliabilityDouble() {

		WeibullDistribution dist = new WeibullDistribution(4.0, 367.0);

		if (Math.abs(dist.reliability(330.0) - 0.5201075851489662850237) >= 1e-6)
			fail("Value non exact!");

		if (Math.abs(dist.reliability(480.0) - 0.0536015295768919997613) >= 1e-6)
			fail("Value non exact!");

	}

	@Test
	public void testReliabilityDoubleArray() {

		double x[] = { 0.8088970536348840927235, 0.7205935727581281005847,
				0.6132727406148557439606 };

		WeibullDistribution dist = new WeibullDistribution(5.0, 15.0);
		double vals[] = dist.reliability(new double[] { 11, 12, 13 });

		for (int i = 0; i < x.length; i++)
			if (Math.abs(vals[i] - x[i]) >= 1e-6)
				fail("Error!");

	}

	@Test
	public void testConditionalReliabilityDoubleDouble() {

		WeibullDistribution dist = new WeibullDistribution(4.0, 367.0);

		if (Math.abs(dist.conditionalReliability(20.0, 330.0)
				- (0.4372741051585738151745 / 0.5201075851489662850237)) >= 1e-6)
			fail("Value non exact!");

	}

	@Test
	public void testConditionalReliabilityDoubleArrayDoubleArray() {

		double x[] = { 0.7205935727581281005847 / 0.8088970536348840927235,
				0.6132727406148557439606 / 0.7205935727581281005847,
				0.492507496725677133987 / 0.6132727406148557439606 };

		WeibullDistribution dist = new WeibullDistribution(5.0, 15.0);
		double vals[] = dist.conditionalReliability(new double[] { 1, 1, 1 },
				new double[] { 11, 12, 13 });

		for (int i = 0; i < x.length; i++)
			if (Math.abs(vals[i] - x[i]) >= 1e-6)
				fail("Error!");

	}

	@Test
	public void testHazardDouble() {

		WeibullDistribution dist = new WeibullDistribution(4.0, 367.0);

		if (Math.abs(dist.hazard(330.0)
				- (0.004121266903639838454089 / 0.5201075851489662850237)) >= 1e-6)
			fail("Value non exact!");

		if (Math.abs(dist.hazard(480.0)
				- (0.001307063326360111375928 / 0.0536015295768919997613)) >= 1e-6)
			fail("Value non exact!");

	}

	@Test
	public void testHazardDoubleArray() {

		double x[] = { 0.07797900748818658766462 / 0.8088970536348840927235,
				0.09838504246724308999983 / 0.7205935727581281005847,
				0.1153295983190182380462 /0.6132727406148557439606 };

		WeibullDistribution dist = new WeibullDistribution(5.0, 15.0);
		double vals[] = dist.hazard(new double[] { 11, 12, 13 });

		for (int i = 0; i < x.length; i++)
			if (Math.abs(vals[i] - x[i]) >= 1e-6)
				fail("Error!");

	}

}
