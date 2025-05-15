export default function Input({
    type = "text",
    value,
    onChange,
    placeholder,
    disabled = false,
    error = "",
    className = "",
}) {
    const baseStyle = "border rounded px-3 py-1.5 text-base focus:outline-none focus:ring-2 focus:ring-green-500 w-full"

    return (
        <div className={`relative w-full ${className}`}>
            <input 
                type={type}
                value={value}
                onChange={onChange}
                placeholder={placeholder}
                disabled={disabled}
                className={`${baseStyle}
                    ${error ? "border-red-500" : 'border-gray-300'}`}
            />
            {error && (
                <p className="absolute left-0 top-full mt-0.5 text-sm text-red-500">
                    {error}
                </p>
            )}
        </div>
    )
}