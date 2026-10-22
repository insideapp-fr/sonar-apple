/*
 * SonarQube Apple Plugin - Enables analysis of Swift and Objective-C projects into SonarQube.
 * Copyright © 2022 inside|app (contact@insideapp.fr)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package fr.insideapp.sonarqube.apple.mobsfscan.runner;

import fr.insideapp.sonarqube.apple.commons.SonarProjectConfiguration;
import org.sonar.api.scanner.ScannerSide;

import java.util.ArrayList;
import java.util.List;

@ScannerSide
public final class MobSFScanRunner extends MobSFScanRunnable {

    private final SonarProjectConfiguration sonarProjectConfiguration;

    public MobSFScanRunner(
            final SonarProjectConfiguration sonarProjectConfiguration
    ) {
        super("mobsfscan");
        this.sonarProjectConfiguration = sonarProjectConfiguration;
    }

    @Override
    protected String[] arguments() {
        List<String> options = new ArrayList<>();
        options.add("--json");
        options.addAll(sonarProjectConfiguration.sources());
        return options.stream().toArray(String[]::new);
    }

    @Override
    protected Integer[] exitCodes() {
        // mobsfscan exits with 1 when it reports findings, which is a successful scan
        return new Integer[]{0, 1};
    }
}
