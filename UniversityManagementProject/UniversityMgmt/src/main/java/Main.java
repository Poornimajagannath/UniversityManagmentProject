import view.AdminController;

public class Main {

	public static void main(String[] args) {
		
		//TODO : Change the path here 
		AdminController controller = new AdminController("/Users/badari/DEV/Poo/projects/UniversityManagementProject/UniversityMgmt/src/test/resources/test_case_0");
		String state = "initial";
//		System.out.println(args);
		if (args != null && !args.equals("")) {
			state = args[0];
		}
		controller.run(state);
	}

}
