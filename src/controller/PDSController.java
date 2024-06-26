package controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import oracle.net.ns.SessionAtts;
import print.Print;
import service.PDSService;
import util.ScanUtil;
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
	Calendar calendar = Calendar.getInstance();

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

	public View mainMenu() {
		if (MainController.sessionStorage.containsKey("member")
				&& MainController.sessionStorage.containsKey("library")) {
			return View.MAIN_ALL;
		}
		if (MainController.sessionStorage.containsKey("member")) {
			return View.MAIN_MEMBER;
		}
		if (MainController.sessionStorage.containsKey("library")) {
			return View.MAIN_LIBRARY;
		}
		return View.MAIN;
	}

	/**
	 * @return 예약된 좌석가능 좌석을 띄움 자신이 예약한 경우 색으로 표시
	 */

	public View pds() {
		if (!MainController.sessionStorage.containsKey("library")) {
			noticeLibrarySel();
			MainController.sessionStorage.put("View", View.PDS);
			return View.LIBRARY;
		}
		Map<String, Object> library = (Map<String, Object>) MainController.sessionStorage.get("library");
		String name = (String) library.get("LIB_NAME");
		printMenuOverVar();
		System.out.println(tap + "\t\t\t\t📖" + name);
		printMenuVar();

		// 자료실 좌석 리스트 출력
		int memNo = 0;
		int libNo = libraryNo();
		String libName = libraryName();
		if (MainController.sessionStorage.containsKey("member")) {
			Map<String, Object> map = (Map<String, Object>) MainController.sessionStorage.get("member");
			memNo = ((BigDecimal) map.get("MEM_NO")).intValue();
		}
		int seatCount = pdsService.seatCount(libNo);
		List<Map<String, Object>> list = pdsService.pdsSeat(libNo);
//		 좌석 정보가 없는 경우, 모든 좌석을 비어있는 상태로 출력합니다.
		if (list == null || list.isEmpty()) {
			System.out.println("\t\t\t\t좌석 이름\t9시\t10시\t11시\t12시\t13시\t14시\t15시\t16시\t17시\t18시\t19시\t20시\t21시");
			System.out.println("───────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────");

			for (int seat = 1; seat <= seatCount; seat++) {
				System.out.print("\t\t\t\tPC " + seat + "\t"); // 자리 이름
				for (int time = 9; time <= 21; time++) {
					System.out.print("□\t");
				}
				System.out.println("");
				if (seat % 10 == 0) {
					System.out.println();
				}
			}
			// 좌석 정보가 있을 때
		} else {
			int[][] seatStatus = new int[seatCount + 1][21 - 9 + 1];
			for (Map<String, Object> map : list) {
				int seatNo = Integer.parseInt(((String) map.get("SEAT_NAME")).replace("PC", ""));
				int hour = ((BigDecimal) map.get("SEAT_REF_HOUR")).intValue();
				int memberNo = ((BigDecimal) map.get("MEM_NO")).intValue();
				seatStatus[seatNo - 1][hour - 9] = (memberNo == memNo) ? 2 : 1;
			}
			System.out.println("\t\t\t\t좌석 이름\t9시\t10시\t11시\t12시\t13시\t14시\t15시\t16시\t17시\t18시\t19시\t20시\t21시");
			System.out.println("───────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────");

			for (int seat = 1; seat <= seatCount; seat++) {
				System.out.print("\t\t\t\tPC " + seat + "\t"); // 자리 이름
				for (int time = 9; time <= 21; time++) {
					if (seatStatus[seat - 1][time - 9] == 0) {
						System.out.print("□\t"); // 빈 좌석
					} else if (seatStatus[seat - 1][time - 9] == 1) {
						System.out.print("■\t"); // 예약된 좌석
					} else {
						System.out.print(GREEN + "■\t" + END); // 자신이 예약한 좌석
					}
				}
				System.out.println();
				System.out.println("\t\t\t\t───────────────────────────────────────────────────────────────────────────────────────────────────────────────");
				if (seat % 10 == 0) {
					System.out.println();
				}
			}
		}
		printMenuVar();
		
		System.out.println(tap + "1.좌석 예약\t\t2.좌석 예약 취소\t\t0.홈");
		printMenuVar();
		int sel = ScanUtil.menu();
		switch (sel) {
		case 1:
			return View.PDS_RESERVATION;
		case 2:
			printMenuOverVar();
			System.out.println(notice+"   자료실 좌석 예약 취소");
			System.out.println();
			System.out.println(tap+"자료실 좌석 예약 취소는 지난시간을 포함하지 않습니다.");
			printMenuVar();
			System.out.println(tap + "1.좌석 예약 전체 취소\t\t2.좌석 부분 예약 취소(좌석별, 시간별)\t\t0.홈");
			printMenuVar();
			int part = ScanUtil.menu();
			switch (part) {
			case 1:
				return View.PDS_CANCEL;
			case 2:
				MainController.sessionStorage.put("part", 1);
				return View.PDS_CANCEL;
			case 0:
				return mainMenu();
			default:
				return View.PDS;
			}
		case 0:
			return mainMenu();
		default:
			return View.PDS;
		}
	}

	public View pdsReserve() {
		if (!MainController.sessionStorage.containsKey("member")) {
			noticeMemberSel();
			MainController.sessionStorage.put("View", View.PDS_RESERVATION);
			return View.LOGIN;
		}
		Map<String, Object> map = (Map<String, Object>) MainController.sessionStorage.get("member");
		int memNo = ((BigDecimal) map.get("MEM_NO")).intValue();
		int libNo = libraryNo();
		String libName = libraryName();

		printMenuVar();
		int seatCount = pdsService.seatCount(libNo);
		int seatNo = 0;
		int startTime = 0;
		int endTime = 0;
		printMenuVar();
		System.out.println(notice+"\t자료실 좌석 예약");
		System.out.println();
		System.out.println(tap+"만약 9시에 대여할 경우 9~10시 사이 이용이 가능합니다");
		System.out.println(tap+"예약은 현재 시각 이후부터만 예약이 가능합니다.");
		System.out.println(tap+"또한 같은 시간에 다른 좌석을 예약할 수 없습니다");
		System.out.println();
		System.out.println(tap+"1시간을 이용할 경우 시작시간과 같은 시간을 입력해주세요");
		printMenuVar();
		System.out.println();
		System.out.println(var);
		System.out.println(notice + "\t예약할 좌석의 번호를 입력 해주세요");
		System.out.println(var);
		while (true) {
			seatNo = ScanUtil.menu();
			if (seatNo >= 1 && seatNo <= seatCount) {
				break;
			}
			noticeNotNo();
		}
		int now = calendar.get(Calendar.HOUR_OF_DAY); // 현재 시간의 시간(hour) 정보를 가져옵니다.

		// 시작시간은 현재 시간 이후로만 가능
		System.out.println(var);
		System.out.println(notice + "\t예약할 시작 시간을 입력해주세요");
		System.out.println(var);
		while (true) {
			startTime = ScanUtil.menu();
			if (startTime >= now && startTime <= 21) {
				break;
			} else if (startTime >= 9 && startTime < now) {
				System.out.println("\t"+RED + var + END);
				System.out.println(notice + "\t지난 시간은 예약이 불가능 합니다");
				System.out.println("\t"+RED + var + END);
			} else {
				System.out.println("\t"+RED + var + END);
				System.out.println(notice + "\t" + now + "시~21시 사이의 시간을 입력해주세요");
				System.out.println("\t"+RED + var + END);
			}
		}
		System.out.println(var);
		System.out.println(notice+"\t예약할 마지막 시간을 입력해주세요");
		System.out.println(var);
		while (true) {
			endTime = ScanUtil.menu();
			if (endTime >= startTime && endTime <= 21) {
				break;
			} else if (endTime > startTime) {
				System.out.println("\t"+RED + var + END);
				System.out.println(notice+"\t시작시간보다 큰 값을 입력해주세요");
				System.out.println("\t"+RED + var + END);
			} else {
				System.out.println("\t"+RED + var + END);
				System.out.println(notice+"\t"+startTime + "시~21시 사이의 시간을 입력해주세요");
				System.out.println("\t"+RED + var + END);
			}
		}
		// 예약여부 확인
		// 같은 도서관에서 같은 시간대에 예약한 좌석 확인
		boolean pdsRentChk = true;
		boolean pdsTimeDupChk = true;
		for (int time = startTime; time <= endTime; time++) {
			List<Object> param = new ArrayList<Object>();
			param.add(time);
			pdsRentChk = pdsService.pdsRentChk(param, libNo, seatNo);
			List<Object> dup = new ArrayList<Object>();
			dup.add(time);
			dup.add(memNo);
			pdsTimeDupChk = pdsService.pdsTimeDupChk(dup, libNo);
			if (!pdsRentChk) {
				System.out.println("\t"+RED + var + END);
				System.out.println(notice + "\t이미 예약된 좌석입니다.");
				System.out.println(notice + "\t확인하고 다시 시도해주세요");
				System.out.println("\t"+RED + var + END);
				return View.PDS;
			}
			if (!pdsTimeDupChk) {
				System.out.println("\t"+RED + var + END);
				System.out.println(notice + "\t같은 시간에 예약한 좌석이 있습니다");
				System.out.println(notice + "\t확인하고 다시 시도해주세요");
				System.out.println("\t"+RED + var + END);
				return View.PDS;
			}
		}
		System.out.println();

		// 예약하기

		for (int time = startTime; time <= endTime; time++) {
			List<Object> param = new ArrayList<Object>();
			param.add(memNo);
			param.add(time);
			pdsService.pdsRent(param, libNo, seatNo);
		}
		System.out.println(var);
		System.out.println(notice + "\t예약이 완료되었습니다.");
		System.out.println(var);
		return View.PDS;
	}

	/**
	 * 전체 예약 취소
	 * 
	 * @return
	 */
	public View pdsCancel() {
		Map<String, Object> map = (Map<String, Object>) MainController.sessionStorage.get("member");
		int memNo = ((BigDecimal) map.get("MEM_NO")).intValue();
		int libNo = libraryNo();
		List<Object> param = new ArrayList<Object>();
		param.add(memNo);
		param.add(libNo);
		// 전체 취소인 경우
		if (!MainController.sessionStorage.containsKey("part")) {
			System.out.println("\t"+RED + var + END);
			System.out.println(notice + "   모든 좌석의 시간대의 예약이 취소됩니다.");
			System.out.println(notice + "   정말 모두 취소하시겠습니까?");
			System.out.println("\t"+RED + var + END);
			while (true) {
				String answer = ScanUtil.nextLine(tap+" Y / N \t");
				if (answer.equalsIgnoreCase("Y")||answer.equals("<")) {

					pdsService.pdsRentCancelAll(param);
					System.out.println(var);
					System.out.println(notice+"   취소가 완료되었습니다.");
					System.out.println(var);
					return View.PDS;
				} else if (answer.equalsIgnoreCase("N")) {
					return View.PDS;
				}
			}
		}
		MainController.sessionStorage.remove("part");
		boolean pdsResChk = true;
		// 부분 취소 좌석별 / 시간대별
		printMenuVar();
		System.out.println(tap + "1. 좌석별 전체 취소\t\t2.시간별 부분 취소");
		printMenuVar();
		int sel = ScanUtil.menu();
		switch (sel) {
		case 1:
			int seatNo = ScanUtil.menu();
			param.add(seatNo);
			pdsResChk = pdsService.pdsResChk(param, sel);
			if (pdsResChk) {
				System.out.println(var);
				System.out.println(notice + "   PC " + seatNo + " 좌석의 현재 시간 이후의 예약이 취소되었습니다.");
				System.out.println(var);
				pdsService.pdsRentCancel(param, sel);
				return View.PDS;
			} else {
				System.out.println("\t"+RED + var + END);
				System.out.println(notice + "   예약한 정보가 없습니다");
				System.out.println(notice + "   다시 확인하고 시도해주세요");
				System.out.println("\t"+RED + var + END);
				return View.PDS;
			}

		case 2:
			int hour = 0;
			while (true) {
				System.out.println(var);
				System.out.println(tap + "취소할 시간대를 입력해주세요");
				System.out.println(var);
				hour = ScanUtil.menu();
				int now = calendar.get(Calendar.HOUR_OF_DAY);
				if (hour >= now && hour <= 21) {
					break;
				}
				System.out.println("\t"+RED + var + END);
				System.out.println(notice + "   " + now + "시~21시 사이의 시간을 입력해주세요");
				System.out.println("\t"+RED + var + END);
			}
			List<Object> param2 = new ArrayList<Object>();
			param2.add(memNo);
			param2.add(libNo);
			param2.add(hour);
			pdsResChk = pdsService.pdsResChk(param2, sel);
			if (pdsResChk) {
				System.out.println(var);
				System.out.println(notice + "   "+hour + "시의 예약이 취소되었습니다.");
				System.out.println(var);
				pdsService.pdsRentCancel(param2, sel);
				return View.PDS;
			} else {
				System.out.println("\t"+RED + var + END);
				System.out.println(notice + "   예약한 정보가 없습니다");
				System.out.println(notice + "   다시 확인하고 시도해주세요");
				System.out.println("\t"+RED + var + END);
				return View.PDS;
			}

		default:
			return View.PDS_CANCEL;
		}

	}
}
