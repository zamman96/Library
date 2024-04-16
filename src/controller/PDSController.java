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
			return View.LIBRARY;
		}
		Map<String, Object> library = (Map<String, Object>) MainController.sessionStorage.get("library");
		String name = (String) library.get("LIB_NAME");
		printMenuOverVar();
		System.out.println("\t\t\t\t📖" + name);
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
		// 좌석 정보가 없는 경우, 모든 좌석을 비어있는 상태로 출력합니다.
		if (list == null || list.isEmpty()) {
			for (int seat = 1; seat <= seatCount; seat++) {
				System.out.print("PC" + seat + "\t"); // 자리 이름
				for (int time = 9; time <= 21; time++) {
					System.out.print("□ ");
				}
				System.out.println("");
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
			for (int seat = 1; seat <= seatCount; seat++) {
				System.out.print("PC " + seat + "\t"); // 자리 이름
				for (int time = 9; time <= 21; time++) {
					if (seatStatus[seat][time - 9] == 0) {
						System.out.print("□ "); // 빈 좌석
					} else if (seatStatus[seat][time - 9] == 1) {
						System.out.print("■ "); // 예약된 좌석
					} else {
						System.out.print(GREEN + "■ " + END); // 자신이 예약한 좌석
					}
				}
				System.out.println();
				if (seat % 10 == 0) {
					System.out.println();
				}
			}
		}
		System.out.println("\t\t1.좌석 예약\t\t2.좌석 예약 취소\t\t0.홈");
		int sel = ScanUtil.menu();
		switch (sel) {
		case 1:
			return View.PDS_RESERVATION;
		case 2:
			System.out.println("1.좌석 예약 전체 취소\t2.좌석 부분 예약 취소\t0.홈");
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
		System.out.println("예약할 좌석의 번호를 입력 해주세요");
		while (true) {
			seatNo = ScanUtil.nextInt("번호 입력 : ");
			if (seatNo >= 1 && seatNo <= seatCount) {
				break;
			}
			System.out.println("없는 번호입니다");
		}
		int now = calendar.get(Calendar.HOUR_OF_DAY); // 현재 시간의 시간(hour) 정보를 가져옵니다.

		// 시작시간은 현재 시간 이후로만 가능
		System.out.println("예약할 시작 시간을 입력해주세요");
		while (true) {
			startTime = ScanUtil.nextInt("시작 시간 입력 : ");
			if (startTime >= now && startTime <= 21) {
				break;
			} else if (startTime >= 9 && startTime < now) {
				System.out.println("지난 시간은 예약이 불가능 합니다");
			} else {
				System.out.println(now + "시~21시 사이의 시간을 입력해주세요");
			}
		}
		System.out.println("예약할 마지막 시간을 입력해주세요");
		while (true) {
			endTime = ScanUtil.nextInt("번호 입력 : ");
			if (endTime >= startTime && endTime <= seatCount) {
				break;
			} else if (endTime > startTime) {
				System.out.println("시작시간보다 큰 값을 입력해주세요");
			} else {
				System.out.println(startTime + "시~21시 사이의 시간을 입력해주세요");
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
				System.out.println("이미 예약된 좌석입니다.");
				System.out.println("확인하고 다시 시도해주세요");
				return View.PDS;
			}
			if (!pdsTimeDupChk) {
				System.out.println("같은 시간에 예약한 좌석이 있습니다");
				System.out.println("확인하고 다시 시도해주세요");
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

		System.out.println("예약이 완료되었습니다.");
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
			System.out.println("모든 좌석의 시간대의 예약이 취소됩니다.");
			System.out.println("정말 모두 취소하시겠습니까?");
			while (true) {
				String answer = ScanUtil.nextLine(" Y / N \t");
				if (answer.equalsIgnoreCase("Y")) {

					pdsService.pdsRentCancelAll(param);
					System.out.println("취소가 완료되었습니다.");
					return View.PDS;
				} else if (answer.equalsIgnoreCase("N")) {
					return View.PDS;
				}
			}
		}
		MainController.sessionStorage.remove("part");
		boolean pdsResChk=true;
		// 부분 취소 좌석별 / 시간대별
		System.out.println("1. 좌석별 전체 취소\t\t2.좌석별 시간 부분 취소");
		int sel = ScanUtil.menu();
		switch (sel) {
		case 1:
			int seatNo = ScanUtil.nextInt("좌석번호입력");
			pdsResChk = pdsService.pdsResChk(param, seatNo, sel);
			if(pdsResChk) {
				System.out.println("PC "+seatNo+" 좌석의 현재 시간 이후의 예약이 취소되었습니다.");
			} else {
				System.out.println("예약한 정보가 없습니다");
				System.out.println("다시 확인하고 시도해주세요");
				return View.PDS;
			}
			
		case 2:
			int seatNo2 = ScanUtil.nextInt("좌석번호입력 : ");
			int hour =0;
			while(true) {
			hour = ScanUtil.nextInt("취소할 시간 입력 : ");
			int now = calendar.get(Calendar.HOUR_OF_DAY);
			if(hour>=now&&hour<=21) {
				break;
				}
			System.out.println(now + "시~21시 사이의 시간을 입력해주세요");
			}
			List<Object> param2 = new ArrayList<Object>();
			param2.add(memNo);
			param2.add(libNo);
			param2.add(hour);
			pdsResChk = pdsService.pdsResChk(param2, seatNo2, sel);
			if(pdsResChk) {
				System.out.println("PC "+seatNo2+" 좌석의 "+hour+"시의 예약이 취소되었습니다.");
				return View.PDS;
			} else {
				System.out.println("예약한 정보가 없습니다");
				System.out.println("다시 확인하고 시도해주세요");
				return View.PDS;
			}

		default:
			return View.PDS_CANCEL;
		}
		
	}
}
