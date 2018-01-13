package rp.assignments.individual.ex1;

import lejos.robotics.RangeFinder;
import lejos.robotics.navigation.DifferentialPilot;
import rp.robotics.DifferentialDriveRobot;
import rp.robotics.MobileRobot;
import rp.systems.StoppableRunnable;
import rp.util.Rate;

/**
 * 
 * A placeholder to show you how you could start writing a controller for the
 * first part of the first individual assignment (creating a controller which
 * drives in a pentagon). Note that you don't have to follow this structure for
 * your controller as there are more elegant and efficient (at least in terms of
 * numbers of lines of code) in which you can implement the different shape
 * controllers.
 * 
 * @author Nick Hawes
 *
 */
public class PentagonController implements StoppableRunnable {

	private final DifferentialDriveRobot m_robot;
	private boolean m_running = false;
	private final DifferentialPilot m_pilot;
	private boolean m_bumped = false;
	private RangeFinder m_ranger;
	private final float m_sideLength;
	
	public PentagonController(DifferentialDriveRobot _robot, float _sideLength) {
		m_robot = _robot;
		m_pilot = m_robot.getDifferentialPilot();
		m_sideLength = _sideLength;	
	}

	@Override
	public void run() {
		m_running = true;
		
		m_pilot.setTravelSpeed(0.20f);
		m_pilot.setRotateSpeed(30);
		
		Rate r;
		while(m_running) {
			float move = m_sideLength;
			float turn = 72f;
			m_pilot.travel(move , true);
			r = new Rate(40);
			while (m_pilot.isMoving() && !m_bumped) {
				if (m_ranger != null) {
					if (m_ranger.getRange() < m_robot.getRobotLength()) {
						System.out.println("Watch out for that wall!");
					}
				}
				r.sleep();
			}

			if (m_bumped) {
				m_pilot.stop();
				m_pilot.travel(-move / 2);

				m_bumped = false;
			}
			m_pilot.rotate(turn);
		}
	}

	@Override
	public void stop() {
		m_running = false;
	}
	
	public void setRangeScanner(RangeFinder _ranger) {
		m_ranger = _ranger;
	}


}
