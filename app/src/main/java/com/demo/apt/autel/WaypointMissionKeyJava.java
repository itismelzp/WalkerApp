package com.demo.apt.autel;


/**
 * Created by lizhiping on 2022/10/1.
 * <p>
 * description
 */
public class WaypointMissionKeyJava {

    ComponentType component = ComponentType.MISSION;

    AutelKeyInfo KeyEnter = new AutelActionKeyInfo(
            component.getValue(),
            MessageTypeConstant.MISSION_WAYPOINT_ENTER_MSG,
            new AutelEmptyConvert(),
            new AutelEmptyConvert()
    ).canPerformAction(true);


}
