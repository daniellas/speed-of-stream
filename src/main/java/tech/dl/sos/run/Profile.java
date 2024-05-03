package tech.dl.sos.run;

import lombok.Getter;

public enum Profile {
	DEFAULT {
		@Getter
		String[] jvmArgs = {"-Xms2G", "-Xmx2G"};
	},
	NOJIT {
		@Getter
		String[] jvmArgs = {"-Xms2G", "-Xmx2G", "-Xint"};
	};

	abstract String[] getJvmArgs();
}
