
package org.springframework.samples.petclinic.service;

import java.time.LocalDateTime;
import java.util.Calendar;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.petclinic.model.MeetingDashboard;
import org.springframework.samples.petclinic.repository.MeetingAssistantRepository;
import org.springframework.samples.petclinic.repository.MeetingRepository;
import org.springframework.samples.petclinic.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MeetingDashboardService {

	@Autowired
	private MeetingRepository			meetRepository;

	@Autowired
	private UserRepository				userRepository;

	@Autowired
	private MeetingAssistantRepository	meetAssisRepository;


	@Transactional(readOnly = true)
	public MeetingDashboard getMeetingDashboard() throws DataAccessException {
		Object[] meetingAssisByGenre = this.meetAssisRepository.assistantByGenre(LocalDateTime.now());
		Calendar fecha = Calendar.getInstance();
		fecha.add(Calendar.MONTH, -1);
		int numDias = fecha.getActualMaximum(Calendar.DAY_OF_MONTH);
		Object[] days = new Object[numDias];
		for (int i = 1; i <= numDias; i++) {
			Object[] obj = new Object[2];
			obj[0] = i;
			obj[1] = 0;
			days[i - 1] = obj;
		}
		Object[] meetByDay = this.meetRepository.meetingsByDay(LocalDateTime.now());
		for (Object object : meetByDay) {
			Object[] obj = (Object[]) object;
			int i = (int) obj[0];
			days[i - 1] = obj;
		}
		Object[] assistantByMeeting = this.meetAssisRepository.assistantByMeeting(LocalDateTime.now());
		Integer meetingAssistant = this.meetAssisRepository.numberOfMeetingsAssistant(LocalDateTime.now());
		Integer numMeeting = this.meetRepository.numberOfMeetings(LocalDateTime.now());
		Double userAss = (double) (this.meetAssisRepository.usersAssisted(LocalDateTime.now()).size() * 100 / this.userRepository.numberUser());

		MeetingDashboard meet = new MeetingDashboard();
		meet.setAssistantByGenre(meetingAssisByGenre);
		meet.setAssistantByMeeting(assistantByMeeting);
		meet.setMeetingsByDay(days);
		meet.setNumberOfMeetings(numMeeting);
		meet.setNumberOfMeetingsAssistant(meetingAssistant);
		meet.setUsersAssisted(userAss);
		return meet;
	}

}
