package com.command.mediator.handler;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.command.mediator.cmn.CommandExecutor;
import com.command.mediator.pojo.CobblerResponse;
import com.command.mediator.pojo.BmResponse;
import com.command.mediator.webservice.form.ConfigureDhcpForm;
import com.command.mediator.webservice.form.CreateBareMetalServerForm;

@Service
public class CobblerHandler extends BaseHandler{

	private static final Logger LOGGER = LoggerFactory.getLogger(CobblerHandler.class);
	
	@Resource
	private CommandExecutor commandExecutor;
	
	private String KICKSTART_DIR ="/var/lib/cobbler/kickstarts/";
	private String CD_COMMAND ="cd ";
	private String COBBLER_PROFILE_COMMAND ="cobbler profile list";
	private String COBBLER_REBOOT_COMMAND ="cobbler system reboot --name=";
	private String DHCP_CONFIGURE_DIR = "/home/neo/scripts/";
	
	public CobblerResponse getKickStartFiles() {
		StringBuilder response= new StringBuilder();
		String result = commandExecutor.execute(CD_COMMAND+KICKSTART_DIR+";ls");
		String files[] = result.split("\\n");
		for(String file: files) {
			response.append(KICKSTART_DIR+file+", ");
		}
		CobblerResponse responseObj = new CobblerResponse(response.toString(),response.toString());
		return responseObj;
	}

	public CobblerResponse getCobblerProfiles() {
		StringBuilder response= new StringBuilder();
		String result = commandExecutor.execute(COBBLER_PROFILE_COMMAND);
		String profiles[] = result.split("\\n");
		for(String profile: profiles) {
			response.append(profile+",");
		}
		CobblerResponse responseObj = new CobblerResponse(response.toString(),response.toString());
		return responseObj;
	}

	public BmResponse createBareMetalServer(CreateBareMetalServerForm serverForm) {
		String command = getAddCobblerCommand(serverForm);
		String output = commandExecutor.execute(command);
		if(output != null && output.contains("exception on server")){
			return new BmResponse(false,output);
		}
		//2nd command: cobbler system reboot --name=system-object-name
		String rebbotOutput = commandExecutor.execute(COBBLER_REBOOT_COMMAND+serverForm.getName());
		if(rebbotOutput != null && rebbotOutput.contains("failed to execute")){
			return new BmResponse(false,output);
		}
		return new BmResponse(true,rebbotOutput);
	}

	public BmResponse configureDhcpServer(ConfigureDhcpForm configureDhcpForm) {
		String dhcpCommand = getDhcpCommand(configureDhcpForm);
		String output = commandExecutor.execute(CD_COMMAND+DHCP_CONFIGURE_DIR+";"+dhcpCommand);
		if(output != null && output.contains("failed")){
			return new BmResponse(false, output);
		}
		return new BmResponse(true,output);
	}
	
}
