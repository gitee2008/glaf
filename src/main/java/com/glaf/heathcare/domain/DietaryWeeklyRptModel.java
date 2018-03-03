package com.glaf.heathcare.domain;

public class DietaryWeeklyRptModel implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	protected String name;

	protected DietaryDayRptModel day1;

	protected DietaryDayRptModel day2;

	protected DietaryDayRptModel day3;

	protected DietaryDayRptModel day4;

	protected DietaryDayRptModel day5;

	protected DietaryDayRptModel day6;

	protected DietaryDayRptModel day0;

	protected double heatEnergy;// 热能实际值

	protected double heatEnergyStandard;// 热能标准值

	protected double heatEnergyPercent;// 热能百分比

	protected String heatEnergyEvaluate;// 热能评价

	protected double heatEnergyCarbohydrate;// 碳水热实际值

	protected double heatEnergyCarbohydrateStandard;// 碳水热标准值

	protected double heatEnergyCarbohydratePercent;// 碳水热百分比

	protected String heatEnergyCarbohydrateEvaluate;// 碳水热评价

	protected double heatEnergyFat;// 脂肪热实际值

	protected double heatEnergyFatStandard;// 脂肪热标准值

	protected double heatEnergyFatPercent;// 脂肪热百分比

	protected String heatEnergyFatEvaluate;// 脂肪热评价

	protected double protein;// 蛋白质实际值

	protected double proteinStandard;// 蛋白质标准值

	protected double proteinPercent;// 蛋白质百分比

	protected String proteinEvaluate;// 蛋白质评价

	protected double proteinAnimal;// 动物蛋白质实际值

	protected double proteinAnimalStandard;// 动物蛋白质标准值

	protected double proteinAnimalPercent;// 动物蛋白质百分比

	protected String proteinAnimalEvaluate;// 动物蛋白质评价

	protected double proteinAnimalBeans;// 动物蛋白质实际值

	protected double proteinAnimalBeansStandard;// 动物豆类蛋白质标准值

	protected double proteinAnimalBeansPercent;// 动物豆类百分比

	protected String proteinAnimalBeansEvaluate;// 动物豆类评价

	protected double vitaminA;// 维A实际值

	protected double vitaminAStandard;// 维A标准值

	protected double vitaminAPercent;// 维A百分比

	protected String vitaminAEvaluate;// 维A评价

	protected double vitaminB1;// 维B1实际值

	protected double vitaminB1Standard;// 维B1标准值

	protected double vitaminB1Percent;// 维B1百分比

	protected String vitaminB1Evaluate;// 维B1评价

	protected double vitaminB2;// 维B2实际值

	protected double vitaminB2Standard;// 维B2标准值

	protected double vitaminB2Percent;// 维B2百分比

	protected String vitaminB2Evaluate;// 维B2评价

	protected double vitaminC;// 维C实际值

	protected double vitaminCStandard;// 维C标准值

	protected double vitaminCPercent;// 维C百分比

	protected String vitaminCEvaluate;// 维C评价

	protected double vitaminE;// 维E实际值

	protected double vitaminEStandard;// 维E标准值

	protected double vitaminEPercent;// 维E百分比

	protected String vitaminEEvaluate;// 维E评价

	protected double calcium;// 钙实际值

	protected double calciumStandard;// 钙标准值

	protected double calciumPercent;// 钙百分比

	protected String calciumEvaluate;// 钙评价

	protected double iron;// 铁实际值

	protected double ironStandard;// 铁标准值

	protected double ironPercent;// 铁百分比

	protected String ironEvaluate;// 铁评价

	protected double zinc;// 锌实际值

	protected double zincStandard;// 锌标准值

	protected double zincPercent;// 锌百分比

	protected String zincEvaluate;// 锌评价

	protected double iodine;// 碘实际值

	protected double iodineStandard;// 碘标准值

	protected double iodinePercent;// 碘百分比

	protected String iodineEvaluate;// 碘评价

	protected double phosphorus;// 磷实际值

	protected double phosphorusStandard;// 磷标准值

	protected double phosphorusPercent;// 磷百分比

	protected String phosphorusEvaluate;// 磷评价

	protected double nicotinicCid;// 尼克酸实际值

	protected double nicotinicCidStandard;// 尼克酸标准值

	protected double nicotinicCidPercent;// 尼克酸百分比

	protected String nicotinicCidEvaluate;// 尼克酸评价

	public DietaryWeeklyRptModel() {

	}

	public double getCalcium() {
		return calcium;
	}

	public String getCalciumEvaluate() {
		return calciumEvaluate;
	}

	public double getCalciumPercent() {
		if (calciumStandard > 0) {
			calciumPercent = Math.round((calcium / calciumStandard * 100D));
		}
		return calciumPercent;
	}

	public double getCalciumStandard() {
		return calciumStandard;
	}

	public DietaryDayRptModel getDay0() {
		return day0;
	}

	public DietaryDayRptModel getDay1() {
		return day1;
	}

	public DietaryDayRptModel getDay2() {
		return day2;
	}

	public DietaryDayRptModel getDay3() {
		return day3;
	}

	public DietaryDayRptModel getDay4() {
		return day4;
	}

	public DietaryDayRptModel getDay5() {
		return day5;
	}

	public DietaryDayRptModel getDay6() {
		return day6;
	}

	public double getHeatEnergy() {
		return heatEnergy;
	}

	public double getHeatEnergyCarbohydrate() {
		return Math.round(heatEnergyCarbohydrate);
	}

	public String getHeatEnergyCarbohydrateEvaluate() {
		return heatEnergyCarbohydrateEvaluate;
	}

	public double getHeatEnergyCarbohydratePercent() {
		if (heatEnergyCarbohydrateStandard > 0) {
			heatEnergyCarbohydratePercent = Math
					.round((heatEnergyCarbohydrate / heatEnergyCarbohydrateStandard * 100D));
		}
		return heatEnergyCarbohydratePercent;
	}

	public double getHeatEnergyCarbohydrateStandard() {
		return heatEnergyCarbohydrateStandard;
	}

	public String getHeatEnergyEvaluate() {
		return heatEnergyEvaluate;
	}

	public double getHeatEnergyFat() {
		return Math.round(heatEnergyFat);
	}

	public String getHeatEnergyFatEvaluate() {
		return heatEnergyFatEvaluate;
	}

	public double getHeatEnergyFatPercent() {
		if (heatEnergyFatStandard > 0) {
			heatEnergyFatPercent = Math.round((heatEnergyFat / heatEnergyFatStandard * 100D));
		}
		return heatEnergyFatPercent;
	}

	public double getHeatEnergyFatStandard() {
		return heatEnergyFatStandard;
	}

	public double getHeatEnergyPercent() {
		if (heatEnergyStandard > 0) {
			heatEnergyPercent = Math.round((heatEnergy / heatEnergyStandard * 100D));
		}
		return heatEnergyPercent;
	}

	public double getHeatEnergyStandard() {
		return heatEnergyStandard;
	}

	public double getIodine() {
		return iodine;
	}

	public String getIodineEvaluate() {
		return iodineEvaluate;
	}

	public double getIodinePercent() {
		if (iodineStandard > 0) {
			iodinePercent = Math.round((iodine / iodineStandard * 100D));
		}
		return iodinePercent;
	}

	public double getIodineStandard() {
		return iodineStandard;
	}

	public double getIron() {
		return iron;
	}

	public String getIronEvaluate() {
		return ironEvaluate;
	}

	public double getIronPercent() {
		if (ironStandard > 0) {
			ironPercent = Math.round((iron / ironStandard * 100D));
		}
		return ironPercent;
	}

	public double getIronStandard() {
		return ironStandard;
	}

	public String getName() {
		return name;
	}

	public double getNicotinicCid() {
		return nicotinicCid;
	}

	public String getNicotinicCidEvaluate() {
		return nicotinicCidEvaluate;
	}

	public double getNicotinicCidPercent() {
		if (nicotinicCidStandard > 0) {
			nicotinicCidPercent = Math.round((nicotinicCid / nicotinicCidStandard * 100D));
		}
		return nicotinicCidPercent;
	}

	public double getNicotinicCidStandard() {
		return nicotinicCidStandard;
	}

	public double getPhosphorus() {
		return phosphorus;
	}

	public String getPhosphorusEvaluate() {
		return phosphorusEvaluate;
	}

	public double getPhosphorusPercent() {
		if (phosphorusStandard > 0) {
			phosphorusPercent = Math.round(phosphorus / phosphorusStandard * 100D);
		}
		return phosphorusPercent;
	}

	public double getPhosphorusStandard() {
		return phosphorusStandard;
	}

	public double getProtein() {
		return protein;
	}

	public double getProteinAnimal() {
		return proteinAnimal;
	}

	public double getProteinAnimalBeans() {
		return proteinAnimalBeans;
	}

	public String getProteinAnimalBeansEvaluate() {
		return proteinAnimalBeansEvaluate;
	}

	public double getProteinAnimalBeansPercent() {
		if (proteinAnimalBeansStandard > 0) {
			proteinAnimalBeansPercent = Math.round((proteinAnimalBeans / proteinAnimalBeansStandard * 100D));
		}
		return proteinAnimalBeansPercent;
	}

	public double getProteinAnimalBeansStandard() {
		return proteinAnimalBeansStandard;
	}

	public String getProteinAnimalEvaluate() {
		return proteinAnimalEvaluate;
	}

	public double getProteinAnimalPercent() {
		if (proteinAnimalStandard > 0) {
			proteinAnimalPercent = Math.round((proteinAnimal / proteinAnimalStandard * 100D));
		}
		return proteinAnimalPercent;
	}

	public double getProteinAnimalStandard() {
		return proteinAnimalStandard;
	}

	public String getProteinEvaluate() {
		return proteinEvaluate;
	}

	public double getProteinPercent() {
		if (proteinStandard > 0) {
			proteinPercent = Math.round((protein / proteinStandard * 100D));
		}
		return proteinPercent;
	}

	public double getProteinStandard() {
		return proteinStandard;
	}

	public double getVitaminA() {
		return vitaminA;
	}

	public String getVitaminAEvaluate() {
		return vitaminAEvaluate;
	}

	public double getVitaminAPercent() {
		if (vitaminAStandard > 0) {
			vitaminAPercent = Math.round((vitaminA / vitaminAStandard * 100D));
		}
		return vitaminAPercent;
	}

	public double getVitaminAStandard() {
		return vitaminAStandard;
	}

	public double getVitaminB1() {
		return vitaminB1;
	}

	public String getVitaminB1Evaluate() {
		return vitaminB1Evaluate;
	}

	public double getVitaminB1Percent() {
		if (vitaminB1Standard > 0) {
			vitaminB1Percent = Math.round((vitaminB1 / vitaminB1Standard * 100D));
		}
		return vitaminB1Percent;
	}

	public double getVitaminB1Standard() {
		return vitaminB1Standard;
	}

	public double getVitaminB2() {
		return vitaminB2;
	}

	public String getVitaminB2Evaluate() {
		return vitaminB2Evaluate;
	}

	public double getVitaminB2Percent() {
		if (vitaminB2Standard > 0) {
			vitaminB2Percent = Math.round((vitaminB2 / vitaminB2Standard * 100D));
		}
		return vitaminB2Percent;
	}

	public double getVitaminB2Standard() {
		return vitaminB2Standard;
	}

	public double getVitaminC() {
		return vitaminC;
	}

	public String getVitaminCEvaluate() {
		return vitaminCEvaluate;
	}

	public double getVitaminCPercent() {
		if (vitaminCStandard > 0) {
			vitaminCPercent = Math.round((vitaminC / vitaminCStandard * 100D));
		}
		return vitaminCPercent;
	}

	public double getVitaminCStandard() {
		return vitaminCStandard;
	}

	public double getVitaminE() {
		return vitaminE;
	}

	public String getVitaminEEvaluate() {
		return vitaminEEvaluate;
	}

	public double getVitaminEPercent() {
		if (vitaminEStandard > 0) {
			vitaminEPercent = Math.round((vitaminE / vitaminEStandard * 100D));
		}
		return vitaminEPercent;
	}

	public double getVitaminEStandard() {
		return vitaminEStandard;
	}

	public double getZinc() {
		return zinc;
	}

	public String getZincEvaluate() {
		return zincEvaluate;
	}

	public double getZincPercent() {
		if (zincStandard > 0) {
			zincPercent = Math.round((zinc / zincStandard * 100D));
		}
		return zincPercent;
	}

	public double getZincStandard() {
		return zincStandard;
	}

	public void setCalcium(double calcium) {
		this.calcium = calcium;
	}

	public void setCalciumEvaluate(String calciumEvaluate) {
		this.calciumEvaluate = calciumEvaluate;
	}

	public void setCalciumPercent(double calciumPercent) {
		this.calciumPercent = calciumPercent;
	}

	public void setCalciumStandard(double calciumStandard) {
		this.calciumStandard = calciumStandard;
	}

	public void setDay0(DietaryDayRptModel day0) {
		this.day0 = day0;
	}

	public void setDay1(DietaryDayRptModel day1) {
		this.day1 = day1;
	}

	public void setDay2(DietaryDayRptModel day2) {
		this.day2 = day2;
	}

	public void setDay3(DietaryDayRptModel day3) {
		this.day3 = day3;
	}

	public void setDay4(DietaryDayRptModel day4) {
		this.day4 = day4;
	}

	public void setDay5(DietaryDayRptModel day5) {
		this.day5 = day5;
	}

	public void setDay6(DietaryDayRptModel day6) {
		this.day6 = day6;
	}

	public void setHeatEnergy(double heatEnergy) {
		this.heatEnergy = heatEnergy;
	}

	public void setHeatEnergyCarbohydrate(double heatEnergyCarbohydrate) {
		this.heatEnergyCarbohydrate = heatEnergyCarbohydrate;
	}

	public void setHeatEnergyCarbohydrateEvaluate(String heatEnergyCarbohydrateEvaluate) {
		this.heatEnergyCarbohydrateEvaluate = heatEnergyCarbohydrateEvaluate;
	}

	public void setHeatEnergyCarbohydratePercent(double heatEnergyCarbohydratePercent) {
		this.heatEnergyCarbohydratePercent = heatEnergyCarbohydratePercent;
	}

	public void setHeatEnergyCarbohydrateStandard(double heatEnergyCarbohydrateStandard) {
		this.heatEnergyCarbohydrateStandard = heatEnergyCarbohydrateStandard;
	}

	public void setHeatEnergyEvaluate(String heatEnergyEvaluate) {
		this.heatEnergyEvaluate = heatEnergyEvaluate;
	}

	public void setHeatEnergyFat(double heatEnergyFat) {
		this.heatEnergyFat = heatEnergyFat;
	}

	public void setHeatEnergyFatEvaluate(String heatEnergyFatEvaluate) {
		this.heatEnergyFatEvaluate = heatEnergyFatEvaluate;
	}

	public void setHeatEnergyFatPercent(double heatEnergyFatPercent) {
		this.heatEnergyFatPercent = heatEnergyFatPercent;
	}

	public void setHeatEnergyFatStandard(double heatEnergyFatStandard) {
		this.heatEnergyFatStandard = heatEnergyFatStandard;
	}

	public void setHeatEnergyPercent(double heatEnergyPercent) {
		this.heatEnergyPercent = heatEnergyPercent;
	}

	public void setHeatEnergyStandard(double heatEnergyStandard) {
		this.heatEnergyStandard = heatEnergyStandard;
	}

	public void setIodine(double iodine) {
		this.iodine = iodine;
	}

	public void setIodineEvaluate(String iodineEvaluate) {
		this.iodineEvaluate = iodineEvaluate;
	}

	public void setIodinePercent(double iodinePercent) {
		this.iodinePercent = iodinePercent;
	}

	public void setIodineStandard(double iodineStandard) {
		this.iodineStandard = iodineStandard;
	}

	public void setIron(double iron) {
		this.iron = iron;
	}

	public void setIronEvaluate(String ironEvaluate) {
		this.ironEvaluate = ironEvaluate;
	}

	public void setIronPercent(double ironPercent) {
		this.ironPercent = ironPercent;
	}

	public void setIronStandard(double ironStandard) {
		this.ironStandard = ironStandard;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setNicotinicCid(double nicotinicCid) {
		this.nicotinicCid = nicotinicCid;
	}

	public void setNicotinicCidEvaluate(String nicotinicCidEvaluate) {
		this.nicotinicCidEvaluate = nicotinicCidEvaluate;
	}

	public void setNicotinicCidPercent(double nicotinicCidPercent) {
		this.nicotinicCidPercent = nicotinicCidPercent;
	}

	public void setNicotinicCidStandard(double nicotinicCidStandard) {
		this.nicotinicCidStandard = nicotinicCidStandard;
	}

	public void setPhosphorus(double phosphorus) {
		this.phosphorus = phosphorus;
	}

	public void setPhosphorusEvaluate(String phosphorusEvaluate) {
		this.phosphorusEvaluate = phosphorusEvaluate;
	}

	public void setPhosphorusPercent(double phosphorusPercent) {
		this.phosphorusPercent = phosphorusPercent;
	}

	public void setPhosphorusStandard(double phosphorusStandard) {
		this.phosphorusStandard = phosphorusStandard;
	}

	public void setProtein(double protein) {
		this.protein = protein;
	}

	public void setProteinAnimal(double proteinAnimal) {
		this.proteinAnimal = proteinAnimal;
	}

	public void setProteinAnimalBeans(double proteinAnimalBeans) {
		this.proteinAnimalBeans = proteinAnimalBeans;
	}

	public void setProteinAnimalBeansEvaluate(String proteinAnimalBeansEvaluate) {
		this.proteinAnimalBeansEvaluate = proteinAnimalBeansEvaluate;
	}

	public void setProteinAnimalBeansPercent(double proteinAnimalBeansPercent) {
		this.proteinAnimalBeansPercent = proteinAnimalBeansPercent;
	}

	public void setProteinAnimalBeansStandard(double proteinAnimalBeansStandard) {
		this.proteinAnimalBeansStandard = proteinAnimalBeansStandard;
	}

	public void setProteinAnimalEvaluate(String proteinAnimalEvaluate) {
		this.proteinAnimalEvaluate = proteinAnimalEvaluate;
	}

	public void setProteinAnimalPercent(double proteinAnimalPercent) {
		this.proteinAnimalPercent = proteinAnimalPercent;
	}

	public void setProteinAnimalStandard(double proteinAnimalStandard) {
		this.proteinAnimalStandard = proteinAnimalStandard;
	}

	public void setProteinEvaluate(String proteinEvaluate) {
		this.proteinEvaluate = proteinEvaluate;
	}

	public void setProteinPercent(double proteinPercent) {
		this.proteinPercent = proteinPercent;
	}

	public void setProteinStandard(double proteinStandard) {
		this.proteinStandard = proteinStandard;
	}

	public void setVitaminA(double vitaminA) {
		this.vitaminA = vitaminA;
	}

	public void setVitaminAEvaluate(String vitaminAEvaluate) {
		this.vitaminAEvaluate = vitaminAEvaluate;
	}

	public void setVitaminAPercent(double vitaminAPercent) {
		this.vitaminAPercent = vitaminAPercent;
	}

	public void setVitaminAStandard(double vitaminAStandard) {
		this.vitaminAStandard = vitaminAStandard;
	}

	public void setVitaminB1(double vitaminB1) {
		this.vitaminB1 = vitaminB1;
	}

	public void setVitaminB1Evaluate(String vitaminB1Evaluate) {
		this.vitaminB1Evaluate = vitaminB1Evaluate;
	}

	public void setVitaminB1Percent(double vitaminB1Percent) {
		this.vitaminB1Percent = vitaminB1Percent;
	}

	public void setVitaminB1Standard(double vitaminB1Standard) {
		this.vitaminB1Standard = vitaminB1Standard;
	}

	public void setVitaminB2(double vitaminB2) {
		this.vitaminB2 = vitaminB2;
	}

	public void setVitaminB2Evaluate(String vitaminB2Evaluate) {
		this.vitaminB2Evaluate = vitaminB2Evaluate;
	}

	public void setVitaminB2Percent(double vitaminB2Percent) {
		this.vitaminB2Percent = vitaminB2Percent;
	}

	public void setVitaminB2Standard(double vitaminB2Standard) {
		this.vitaminB2Standard = vitaminB2Standard;
	}

	public void setVitaminC(double vitaminC) {
		this.vitaminC = vitaminC;
	}

	public void setVitaminCEvaluate(String vitaminCEvaluate) {
		this.vitaminCEvaluate = vitaminCEvaluate;
	}

	public void setVitaminCPercent(double vitaminCPercent) {
		this.vitaminCPercent = vitaminCPercent;
	}

	public void setVitaminCStandard(double vitaminCStandard) {
		this.vitaminCStandard = vitaminCStandard;
	}

	public void setVitaminE(double vitaminE) {
		this.vitaminE = vitaminE;
	}

	public void setVitaminEEvaluate(String vitaminEEvaluate) {
		this.vitaminEEvaluate = vitaminEEvaluate;
	}

	public void setVitaminEPercent(double vitaminEPercent) {
		this.vitaminEPercent = vitaminEPercent;
	}

	public void setVitaminEStandard(double vitaminEStandard) {
		this.vitaminEStandard = vitaminEStandard;
	}

	public void setZinc(double zinc) {
		this.zinc = zinc;
	}

	public void setZincEvaluate(String zincEvaluate) {
		this.zincEvaluate = zincEvaluate;
	}

	public void setZincPercent(double zincPercent) {
		this.zincPercent = zincPercent;
	}

	public void setZincStandard(double zincStandard) {
		this.zincStandard = zincStandard;
	}

}
