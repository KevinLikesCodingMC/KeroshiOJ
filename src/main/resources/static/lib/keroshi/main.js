function renderMath(text) {
	if (! text) return "";
	text = text.replace(/\$\$([\s\S]*?)\$\$/g, (match, formula) => {
		try {
			return katex.renderToString(formula.trim(), {
				displayMode: true,
				throwOnError: false
			});
		} catch (e) {
			console.error("KaTeX block error: ", e);
			return match;
		}
	});
	text = text.replace(/(?<!\\)\$([^$\n]+?)\$(?!\\)/g, (match, formula) => {
		try {
			return katex.renderToString(formula.trim(), {
				displayMode: false,
				throwOnError: false
			});
		} catch (e) {
			console.error("KaTeX block error: ", e);
			return match;
		}
	});
	return text;
}

if (typeof CodeMirror !== 'undefined') {
	CodeMirror.defineMode("ojMarkdown", function (config) {
		return CodeMirror.multiplexingMode(
			CodeMirror.getMode(config, "text/x-markdown"),
			{
				open: "$$", close: "$$",
				mode: CodeMirror.getMode(config, "text/x-stex"),
				delimStyle: "katex-marker"
			},
			{
				open: "$", close: "$",
				mode: CodeMirror.getMode(config, "text/x-stex"),
				delimStyle: "katex-marker"
			}
		);
	});
}
