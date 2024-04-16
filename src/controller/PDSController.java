package controller;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import print.Print;
import service.PDSService;
import util.View;

public class PDSController extends Print {
	private static PDSController instance;

	private PDSController() {

	}

	public static PDSController getInstance() {
		if (instance == null) {
			instance = new PDSController();
		}
		return instance;
	}

	PDSService pdsService = PDSService.getInstance();

	public int libraryNo() {
		Map<String, Object> map = (Map<String, Object>) MainController.sessionStorage.get("library");
		int libNo = ((BigDecimal) map.get("LIB_NO")).intValue();
		return libNo;
	}

	public String libraryName() {
		Map<String, Object> library = (Map<String, Object>) MainController.sessionStorage.get("library");
		String libName = (String) library.get("LIB_NAME");
		return libName;
	}

	/**
	 * @return 예약된 좌석가능 좌석을 띄움 자신이 예약한 경우 색으로 표시
	 */
	public View pdsList() {
//		int memNo = 0;
		int memNo = 12;
		int libNo = 1;
//		int libNo = libraryNo();
//		String libName = libraryName();
		if (MainController.sessionStorage.containsKey("member")) {
			Map<String, Object> map = (Map<String, Object>) MainController.sessionStorage.get("member");
			memNo = ((BigDecimal) map.get("MEM_NO")).intValue();
		}
		printMenuOverVar();
//		System.out.println("\t\t\t\t📖" + libName);
		printMenuVar();

		int seatCount = pdsService.seatCount(libNo);
		List<Map<String, Object>> list = pdsService.pdsSeat(libNo);
		if (list == null) {
			for (int seat = 1; seat <= seatCount; seat++) {
				System.out.print("PC" + seat + "\t"); // 자리 이름
				for (int time = 9; time <= 21; time++) {
					System.out.print("□");
					System.out.print(" ");
				}
				System.out.println("");
			}
		}

		Map<Integer, Integer> seatMap = new HashMap<>(); // 예약되어있는 정보 저장 seat_NO,hour
		Map<Integer, Integer> seatMemMap = new HashMap<>(); // 회원이 예약한 seat_NO,hour 저장
		for (Map<String, Object> map : list) {
			int memberNo = ((BigDecimal) map.get("MEM_NO")).intValue();
			String seatName = (String) map.get("SEAT_NAME");
			int seatNo = Integer.parseInt(seatName.replace("PC", ""));
			int hour = ((BigDecimal) map.get("SEAT_REF_HOUR")).intValue();
			seatMap.put(seatNo, hour);
			if (MainController.sessionStorage.containsKey("member")) {
				if (memberNo == memNo) {
					seatMemMap.put(seatNo, hour);
				}
			}
		}

		for (int seat = 1; seat <= seatCount; seat++) {
			System.out.print("PC" + seat + "\t"); // 자리 이름
			for (int time = 9; time <= 21; time++) {
				if (seatMap.get(seat) == time) {
					System.out.print(seatMemMap.get(seat));
//					if (seatMemMap.get(seat) == time) {
//						System.out.print(GREEN + "■" + END);
//					}
					System.out.print("■");
				} else {
					System.out.print("□");
				}
				System.out.println(" ");
			}
			System.out.println("");
		}

		return null;
	}

	public View pdsReserve() {
		// TODO Auto-generated method stub
		return null;
	}

}
