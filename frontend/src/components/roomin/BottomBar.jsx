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
import { useState } from "react"
import { useNavigate } from "react-router";

export default function BottomBar() {
    const [isMicOn, setIsMicOn] = useState(false);
    const [isCamOn, setIsCamOn] = useState(false);
    const [isScreenSharing, setIsScreenSharing] = useState(false);

    const navigate = useNavigate();

    const onToggleMic = () => {
        setIsMicOn((prev) => !prev);
    }

    const onToggleCam = () => {
        if (isScreenSharing) {
            alert("화면 공유중에는 캠을 킬 수 없습니다.");
            return;
        }
        setIsCamOn((prev) => !prev);
    }

    const onShareScreen = () => {
        if (isCamOn) {
            alert("캠을 키는 동안에는 화면 공유를 할 수 없습니다.");
            return;
        }
        setIsScreenSharing((prev) => !prev);
    }

    const onLeave = () => {
        const confirmLeave = window.confirm("정말 나가시곘어요?");
        if (confirmLeave) {
            setIsMicOn(false);
            setIsCamOn(false);
            setIsScreenSharing(false);
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
                tooltip={isScreenSharing ? "화면 공유 켜기" : "화면 공유 끄기"}
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