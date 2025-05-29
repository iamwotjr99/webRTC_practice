import {
    FaMicrophone,
    FaMicrophoneSlash,
    FaVideo,
    FaVideoSlash,
    FaDesktop,
    FaSignOutAlt,
    FaStop,
} from "react-icons/fa"
import TooltipButton from "../common/TooltipButton"
import { useNavigate } from "react-router";

export default function BottomBar({
    isMicOn,
    isCamOn,
    isScreenSharing,
    onToggleMic,
    onToggleCam,
    onShareScreen,
    leaveRoom,
    roomId,
}) {

    const navigate = useNavigate();

    const onLeave = () => {
        const confirmLeave = window.confirm("정말 나가시곘어요?");
        if (confirmLeave) {
            leaveRoom(roomId);
            navigate("/");
        }
    }

    return (
        <div className="absolute bottom-6 left-1/2 transform -translate-x-1/2 bg-gray-900 bg-opacity-80 rounded-full px-6 py-3 flex items-center gap-12 shadow-lg">
            <TooltipButton 
                icon={isMicOn ? <FaMicrophone /> : <FaMicrophoneSlash />}
                tooltip={isMicOn ? "마이크 끄기" : "마이크 켜기"}
                onClick={onToggleMic}
                color="text-white"
                hoverColor="hover:text-green-400"
            />

            <TooltipButton 
                icon={isCamOn ? <FaVideo /> : <FaVideoSlash />}
                tooltip={isCamOn ? "캠 끄기" : "캠 켜기"}
                onClick={onToggleCam}
                color="text-white"
                hoverColor="hover:text-blue-400"
            />

            <TooltipButton 
                icon={isScreenSharing ? <FaStop /> : <FaDesktop />}
                tooltip={isScreenSharing ? "화면 공유 끄기" : "화면 공유 켜기"}
                onClick={onShareScreen}
                color="text-white"
                hoverColor="hover:text-yellow-400"
            />

            <TooltipButton 
                icon={<FaSignOutAlt />}
                tooltip="방 나가기"
                onClick={onLeave}
                color="text-red-500"
                hoverColor="hover:text-red-400"
            />
        </div>
    )
}