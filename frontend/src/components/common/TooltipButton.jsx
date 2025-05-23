export default function TooltipButton({
    icon,
    tooltip,
    onClick,
    color,
    hoverColor,
}) {
    return (
        <div className="relative group">
            <button className={`text-2xl ${color} ${hoverColor}`} onClick={onClick}>
                {icon}
            </button>
            <span className="absolute bottom-full mb-2 left-1/2 -translate-x-1/2 whitespace-nowrap bg-gray-700 bg-opacity-90 text-white text-sm px-2 py-1 rounded opacity-0 group-hover:opacity-100 transition">
                {tooltip}
            </span>
        </div>
    )
}