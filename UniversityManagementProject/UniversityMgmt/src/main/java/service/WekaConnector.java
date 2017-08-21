package service;

import manager.RecordsManager;
import model.Record;
import model.identity.ID;
import weka.associations.Apriori;
import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.Instance;
import weka.core.Instances;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class WekaConnector {

	static final String TAKEN = "taken";
	static final String NONE = "none";

	private RecordsManager manager;

	private final List<String> attributeValues;

	public WekaConnector(RecordsManager manager) {
		this.manager = manager;
		this.attributeValues = new ArrayList<>();
		this.attributeValues.add(TAKEN);
		this.attributeValues.add(NONE);
	}

	public String analyze() {
		String result = null;
		try {
			List<Record> allRecords = manager.getAll();
			// Get unique students from records
			Set<ID> students = allRecords.stream()
					.map(record -> record.getStudentId())
					.collect(Collectors.toSet());

			ArrayList<Attribute> es = new ArrayList<>();
			// build attributes
			Set<Attribute> collect = allRecords.stream()
					.map(record -> new Attribute(
							"course" + record.getClassId().toString(),
							attributeValues)).collect(Collectors.toSet());
			es.addAll(collect);

			Instances data = new Instances("records", es, students.size());
			for (ID student : students) {
				Instance instance = new DenseInstance(collect.size());
				int i = 0;
				for (Attribute attribute : collect) {
					String courseId = attribute.name()
							.substring(attribute.name().indexOf("e") + 1);
					List<Record> records = manager
							.getForStudentAndClass(student, new ID(courseId));

					if (records != null && records.size() > 0) {
						instance.setValue(attribute, TAKEN);
					} else {
						instance.setValue(attribute, NONE);
					}
					i++;
				}
				data.add(instance);
			}

			// build associator
			Apriori apriori = new Apriori();
			apriori.setClassIndex(data.numAttributes() - 1);

			apriori.buildAssociations(data);

			// output associator
			result = apriori.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;

	}

}
